import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
// SỬA: Import từ users.ts
import { User } from '../models/user';
import { CirculationService } from '../services/circulation.service'; // Use CirculationService instead of BorrowService
import { UsersService } from '../services/users.service';
import { LoanDetails } from '../services/admin.service';

@Component({
    selector: 'app-user-details',
    templateUrl: './user-details.component.html',
    styleUrls: ['./user-details.component.css'],
    standalone: false
})
export class UserDetailsComponent implements OnInit {

  id!: number;
  borrow: LoanDetails[] = [];
  user!: User;

  constructor(
    private route: ActivatedRoute,
    private circulationService: CirculationService,
    public userService: UsersService
  ) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params['userId'];

    this.user = new User();
    this.userService.getUserById(this.id).subscribe((data: User) => {
      this.user = data;
    });

    this.getBorrowedByUser(this.id);
  }

  private getBorrowedByUser(userId: number) {
    this.circulationService.getLoansByMemberId(userId).subscribe((data: LoanDetails[]) => {
      this.borrow = data;
    });
  }
}