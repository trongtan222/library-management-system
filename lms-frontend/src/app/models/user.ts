// Đổi tên từ Users thành User
export class User {
    userId: number;
    username: string;
    name: string;
    email?: string;
    password?: string;
    roles: string[];
}