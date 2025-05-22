export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  mobile: string;
  role: string;
  fullName: string;
}
