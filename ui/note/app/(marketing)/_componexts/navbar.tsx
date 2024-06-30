"use client";

import { LoginDialog } from "@/app/(marketing)/_componexts/draft";
import { Logo } from "@/app/(marketing)/_componexts/logo";
import { GET } from "@/app/utils/fetch";
import { ModeToggle } from "@/components/mode-toggle";
import { LoginProvider } from "@/components/providers/Login-provider";
import { AuthContext } from "@/components/providers/notion-provider";
import { Spinner } from "@/components/spinner";
import { Button } from "@/components/ui/button";
import { useScrollTop } from "@/hooks/use-scroll-top";
import { cn } from "@/lib/utils";
import Link from "next/link";
import { useContext, useEffect } from "react";
import {CHECK_HEALTH} from "@/app/services/comment-api";
export const Navbar = () => {
    const scrolled = useScrollTop();
    const { isAuth, isLoading, setLoading } = useContext(AuthContext);
    useEffect(() => {
        if (isLoading) {
            GET({ url: CHECK_HEALTH }).then((resp) => {
                if (resp.ok) {
                    setLoading(!isLoading);
                }
            });
        }
    }, []);

    return (
        <div
            className={cn(
                "z-50 bg-background dark:bg-[#1F1F1F] fixed top-0 flex items-center w-full p-6",
                scrolled && "border-b shadow-sm",
            )}
        >
            <Logo />
            <div className="md:ml-auto md:justify-end justify-between w-full flex items-center gap-x-2">
                {isLoading && <Spinner />}
                {!isLoading && !isAuth && (
                    <>
                        <LoginProvider>
                            <LoginDialog />
                            <LoginDialog label="Get Notion free" variant="default" size="sm" />
                        </LoginProvider>
                    </>
                )}
                {isAuth && !isLoading && (
                    <>
                        <Button variant="ghost" size="sm" asChild>
                            <Link href="/documents">Enter Notion</Link>
                        </Button>
                    </>
                )}
                <ModeToggle />
            </div>
        </div>
    );
};

//NOTE:is web3 for convex

// export const Navbar = () => {
//     const { isAuthenticated, isLoading } = useConvexAuth();
//     const scrolled = useScrollTop();
//     return (
//         <div
//             className={cn(
//                 "z-50 bg-background dark:bg-[#1F1F1F] fixed top-0 flex items-center w-full p-6",
//                 scrolled && "border-b shadow-sm",
//             )}
//         >
//             <Logo />
//             <div className="md:ml-auto md:justify-end justify-between w-full flex items-center gap-x-2">
//                 {isLoading && <Spinner />}
//                 {!isAuthenticated && !isLoading && (
//                     <>
//                         <SignInButton mode="modal">
//                             <Button variant="ghost" size="sm">
//                                 Log in
//                             </Button>
//                         </SignInButton>
//                         <SignInButton mode="modal">
//                             <Button size="sm">Get Notion free</Button>
//                         </SignInButton>
//                     </>
//                 )}
//                 {isAuthenticated && !isLoading && (
//                     <>
//                         <Button variant="ghost" size="sm" asChild>
//                             <Link href="/documents">Enter Notion</Link>
//                         </Button>
//                         <UserButton afterSignOutUrl="/" />
//                     </>
//                 )}
//                 <ModeToggle />
//             </div>
//         </div>
//     );
// };