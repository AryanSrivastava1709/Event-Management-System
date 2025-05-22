export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  mobile: string;
  role: string;
}

export interface RegisterResponse {
  fullName: string;
  email: string;
  mobile: string;
  role: string;
}
