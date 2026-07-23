import { NextRequest } from "next/server";

const AUTH_TOKEN = process.env.AUTH_TOKEN;

export function validateAuth(req: NextRequest): boolean {
  if (!AUTH_TOKEN) {
    return true;
  }
  const authHeader = req.headers.get("authorization");
  if (!authHeader) {
    return false;
  }
  const token = authHeader.replace("Bearer ", "");
  return token === AUTH_TOKEN;
}
