import { GET } from "@/app/utils/fetch";
import { BaseType } from "@/app/utils/status-type";
import { AuthPopos } from "@/components/providers/notion-provider";

const host = process.env.NEXT_PUBLIC_HOST + "/user";
export async function refreshToken(refreshToken: string | null): Promise<string> {
    if (!refreshToken) throw new Error("refreshToken is null");
    const resp = await GET({
        url: UserURI.refreshToken,
        options: {
            headers: {
                authorization: refreshToken,
            },
        },
    });
    const json: AuthPopos = await resp.json();
    return json.accessToken;
}
export async function getUser(accessToken: string): Promise<User> {
    const resp = await GET({
        url: UserURI.getUserInfo,
        options: {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        },
    });
    return resp.json();
}

export type User = {
    id: number;
    username: string;
    imageUrl: string;
    email: string;
};

export interface UserProps {
    originalUsername: String;
    username: String;
    passwordHash: String;
    email: String;
    appId: number;
    isDelete: Boolean;
    createdAt: String;
    updatedAt: String;
    organize: String;
    id: number;
    code: String;
    password: String;
    roles: ["USER", "ADMIN"];
}
export class UserURI {
    static login = host + "/login";

    static isExistEmailUri(email: string) {
        return host + "/isExist/" + email;
    }
    static sendCodeToEmail(email: string) {
        return host + "/sendCodeToEmail/" + email;
    }
    static isAuth = host + "/isAuth";
    static getUserInfo = host + "/getUserInfo";

    static refreshToken = host + "/refreshToken";
}
// FIX: remove
export const CODE_ERROR = new BaseType(461, "验证码错误");
export const LOGIN_ERROR = new BaseType(460, "登陆异常 请检查 用户名密码");
export const USER_EMAIL_EXITS = new BaseType(207, "用户邮箱已存在");
export const USER_NOT_REGISTER = new BaseType(208, "用户未注册");
export const EMAIL_FORMAT_ERROR = new BaseType(463, "邮箱格式错误");

export const ACCESSDENIED = new BaseType(464, "访问限制");