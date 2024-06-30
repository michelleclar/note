import { Button } from "@/components/ui/button";
import * as React from "react";
import Image from "next/image";
import { Card } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { InputOTP, InputOTPGroup, InputOTPSlot } from "@/components/ui/input-otp";
import { REGEXP_ONLY_DIGITS_AND_CHARS } from "input-otp";

interface LogInProps {
    username: string;
    password: string;
    code: string;
}
function SvgComponent() {
    return (
        <svg
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 10 10"
            aria-hidden="true"
            className="h-2.5 w-2.5 shrink-0"
        >
            <path
                d="M1.791.722a.756.756 0 00-1.07 1.07L3.932 5 .72 8.209a.756.756 0 101.07 1.07L5 6.068l3.209 3.21a.756.756 0 001.07-1.07L6.068 5l3.21-3.209a.756.756 0 10-1.07-1.07L5 3.932 1.791.72z"
                fill="currentColor"
            />
        </svg>
    );
}
export const _LogInCard = () => {
    return (
        <>
            <div className="flex flex-col bg-gray-50 items-center mt-[-0.5rem] pt-2">
                <div className="flex gap-1 mx-auto my-0 flex-row flex-nowrap px-8 py-4 justify-start items-stretch ">
                    <span className="text-[0.8125rem] font-normal leading-[1.38462] text-[rgb(116,118,134)]">
                        Don’t have an account?
                    </span>
                    <a className="box-border inline-flex items-center cursor-pointer no-underline tracking-[normal] font-medium text-[0.8125rem] leading-[1.38462] text-[rgb(47,48,55)] m-0">
                        Sign up
                    </a>
                </div>
            </div>
        </>
    );
};

export const LogInCard = () => {
    const [username, setUsername] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [code, setCode] = React.useState("12345");
    const check = () => {
        return username || password || code;
    };

    const submit = () => {
        if (!check()) return;
        console.log("username: ", username);
        console.log("password: ", password);
        console.log("code", code);
        const data: LogInProps = { username: username, password: password, code: code };
        React.useEffect(() => {
            fetch("http:127.0.0.1:8080/user/login", {
                method: "POST",
                mode: "cors",
                cache: "no-cache",
                credentials: "same-origin",
                headers: { "Content-Type": "application/json" },
                redirect: "follow",
                referrerPolicy: "no-referrer",
                body: JSON.stringify(data),
            })
                .then((res) => res.json)
                .then((rep) => console.log(rep))
                .catch((err) => console.log(err.message));
        }, []);
    };

    return (
        <Card
            /* rounded-lg 圆角效果 border 边框效果 flex-col 布局变为上下*/
            className="w-[400px] h-[270] font-mono"
        >
            <Card className="flex items-stretch gap-8 bg-white px-10 py-8 flex-col flex-nowrap drop-shadow-line text-center z-10 relative">
                <button
                    /* 关闭按钮 */
                    className="inline-flex justify-center items-center absolute right-2 top-2 text-[#747686] p-3"
                >
                    <SvgComponent />
                </button>
                <div className="flex flex-col items-stretch justify-start gap-1 leading-[1.41176]">
                    <h1 className="text-[#212126] text-[1.0625rem] font-bold">Sign in to notion</h1>
                    <p className="text-[#747686] text-[0.8125rem] font-normal leading-[1.38462]">
                        Welcome back! Please sign in to continue
                    </p>
                </div>
                <div className="flex flex-col space-y-1.5">
                    <Label className="text-[0.8125rem] font-normal leading-[1.38462]" htmlFor="name">
                        You can use already created account by notion
                    </Label>
                    <Input
                        id="you name"
                        placeholder="You Name for notion"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <Input
                        type="password"
                        id="you password"
                        placeholder="password for your account"
                        autoComplete="current-password"
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <InputOTP
                        type="password"
                        pattern={REGEXP_ONLY_DIGITS_AND_CHARS}
                        value={code}
                        maxLength={5}
                        onChange={(code) => setCode(code)}
                    >
                        <InputOTPGroup>
                            <InputOTPSlot index={0} />
                            <InputOTPSlot index={1} />
                            <InputOTPSlot index={2} />
                            <InputOTPSlot index={3} />
                            <InputOTPSlot index={4} />
                        </InputOTPGroup>
                    </InputOTP>
                    <Button type="submit" onClick={submit}>
                        Submit
                    </Button>
                </div>
                <div className="box-border grid grid-cols-[repeat(1,1fr)] justify-start gap-2  items-center">
                    <p>
                        or <br /> use other channel account
                    </p>
                    <Button className="drop-shadow-line px-3 py-1.5 h-[30px] " variant="outline">
                        <span className="flex gap-3 justify-center">
                            <span className="flex justify-center items-center">
                                <Image
                                    src="/github.svg"
                                    height="10"
                                    width="10"
                                    alt="github"
                                    className="dark:hidden h-auto w-4 max-w-full"
                                />
                            </span>
                            <span className="text-[#747686] hover:text-sky-400 text-[0.8125rem] font-medium">
                                Continue with GitHub
                            </span>
                        </span>
                    </Button>
                </div>
            </Card>
            <div className="flex flex-col bg-gray-50 items-center mt-[-0.5rem] pt-2">
                <div className="flex gap-1 mx-auto my-0 flex-row flex-nowrap px-8 py-4 justify-start items-stretch ">
                    <span className="text-[0.8125rem] font-normal leading-[1.38462] text-[rgb(116,118,134)]">
                        Don’t have an account?
                    </span>
                    <a className="box-border inline-flex items-center cursor-pointer no-underline tracking-[normal] font-medium text-[0.8125rem] leading-[1.38462] text-[rgb(47,48,55)] m-0">
                        Sign up
                    </a>
                </div>
            </div>
        </Card>
    );
};

export const LogInPage = () => {
    return (
        <div
            /* 添加一个full screen背景 color black  */
            className="flex fixed items-start justify-center border left-0 top-0 bg-[rgba(0,0,0,0.73)] w-screen h-screen box-border"
        >
            <div
                /** 设定card 垂直 中心 位置**/
                className="flex rounded-md mx-0 my-16 bg-card items-start justify-center"
            >
                <LogInCard />
            </div>
        </div>
    );
};
export const LogInButton = () => {
    const [isHidden, setIsHidden] = React.useState(false);

    return (
        <div>
            {isHidden && <LogInPage />}
            <Button onClick={() => setIsHidden(!isHidden)}>Log In</Button>
        </div>
    );
};