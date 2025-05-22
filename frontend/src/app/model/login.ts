export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  userId: string;
  token: string;
  email: string;
  mobile: string;
  role: string;
  fullName: string;
}
