import { Button } from "@/components/ui/button";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import Image from "next/image";
import { ArrowRight } from "lucide-react";

import { useContext } from "react";
import { AuthContext } from "@/components/providers/notion-provider";
import { REFRESH_TOKEN } from "@/app/utils/fileds";
import { refreshToken } from "@/app/services/user/userServers";
export function LoginDialog({
    label,
    variant,
    size,
}: {
    label?: string;
    variant?: "default" | "destructive" | "outline" | "secondary" | "ghost" | "link" | "purple";
    size?: "default" | "sm" | "lg" | "icon";
}) {
    const clientId = "Ov23liXUoSwEMmHNVnU1";
    const callbackUrl = "http://localhost:8080/oauth/github";
    const url = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${callbackUrl}`;

    const { setIsAuth } = useContext(AuthContext);
    const LoginHandler = () => {
        const _refreshToken = localStorage.getItem(REFRESH_TOKEN);
        if (_refreshToken !== null) {
            refreshToken(_refreshToken).then((accentToken) =>
                setIsAuth({ accessToken: accentToken, refreshToken: _refreshToken }),
            );
            return;
        }
        window.location.href = url;
    };
    //TODO if login success show user card

    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button variant={variant ? variant : "ghost"} size={size ? size : "sm"}>
                    {label ? label : "Log in"}

                    {label && <ArrowRight className="h-4 w-4 ml-2" />}
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]  w-[400px] h-[270]">
                <DialogHeader className="flex flex-col items-center justify-start gap-1 leading-[1.41176]">
                    <DialogTitle className="text-[#212126] text-[1.0625rem] font-bold">Sign in to notion</DialogTitle>
                    <DialogDescription className="text-[#747686] text-[0.8125rem] font-normal leading-[1.38462]">
                        Welcome back! Please sign in to continue
                    </DialogDescription>
                </DialogHeader>
                <Button className="drop-shadow-line px-3 py-1.5 h-[30px] " variant="outline" onClick={LoginHandler}>
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
                        <a className="text-[#747686] hover:text-sky-400 text-[0.8125rem] font-medium">
                            Continue with GitHub
                        </a>
                    </span>
                </Button>
                <div className="flex w-full flex-col   items-center mt-[-0.5rem] pt-2">
                    <Label className="text-center w-full justify-center text-[#747686]">
                        Don’t have an account?{" "}
                        <a className="box-border inline-flex items-center cursor-pointer no-underline tracking-[normal] font-medium text-[0.8125rem] leading-[1.38462] text-[rgb(47,48,55)] m-0">
                            Sign up
                        </a>
                    </Label>
                </div>
            </DialogContent>
        </Dialog>
    );
}