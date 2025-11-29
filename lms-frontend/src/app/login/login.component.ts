import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { UsersService } from '../services/users.service';
import { UserAuthService } from '../services/user-auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    standalone: false
})
export class LoginComponent {
  username = '';
  password = '';
  remember = false;
  loading = false;
  error = '';

  constructor(
    private usersService: UsersService,
    private userAuth: UserAuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  // Hàm fallback nếu không cài thư viện jwt-decode
  private safeDecodeJwt(jwt: string): any {
    try {
      const base64Url = jwt.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (e) {
      console.error('Lỗi decode JWT:', e);
      return {};
    }
  }

  onSubmit(f: NgForm) {
    if (f.invalid) return;
    this.error = '';
    this.loading = true;

    const username = String(f.value.username || '').trim().toLowerCase();
    const password = String(f.value.password || '');

    this.usersService.login({ username, password }).subscribe({
      next: (res: any) => {
        const token = typeof res === 'string' ? res : (res?.token || res?.jwt || res?.jwtToken);
        if (!token) {
          this.error = 'Không nhận được token từ server.';
          this.loading = false;
          return;
        }

        this.userAuth.setToken(token);

        // Decode JWT an toàn
        const payload = this.safeDecodeJwt(token);

        const name = res?.name ?? payload?.name ?? payload?.sub ?? username;
        const id = Number(res?.userId ?? payload?.userId ?? payload?.id ?? 0);
        
        // Xử lý roles
        const rawRoles = res?.roles ?? payload?.roles ?? [];
        const roles = (Array.isArray(rawRoles) ? rawRoles : [rawRoles])
          .map((r: any) => typeof r === 'string' ? r.toUpperCase() : r?.authority?.toUpperCase() || '')
          .filter(Boolean)
          .map((r: string) => r.startsWith('ROLE_') ? r : `ROLE_${r}`);

        this.userAuth.setRoles(roles);
        this.userAuth.setUserId(id);
        this.userAuth.setName(name);

        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
        const isAdmin = roles.includes('ROLE_ADMIN');
        
        // Ưu tiên returnUrl, nếu không thì điều hướng theo role
        this.router.navigateByUrl(returnUrl || (isAdmin ? '/admin/dashboard' : '/'));
      },
      error: (err) => {
        console.error(err);
        this.error = err.error?.message || 'Sai tài khoản hoặc mật khẩu.';
        this.loading = false;
      },
      complete: () => (this.loading = false),
    });
  }
}