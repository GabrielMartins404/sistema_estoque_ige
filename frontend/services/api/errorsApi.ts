export class ApiError extends Error {
  public readonly statusCode: number;
  public readonly errors?: { field: string; message: string }[];

  constructor(message: string, statusCode: number, errors?: { field: string; message: string }[]) {
    super(message);
    this.name = 'ApiError';
    this.statusCode = statusCode;
    this.errors = errors;
  }
}