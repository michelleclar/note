"use client";

import { useContext, useEffect } from "react";
import { GET } from "@/app/utils/fetch";
import { Spinner } from "@/components/spinner";
import { AuthContext, AuthPopos } from "@/components/providers/notion-provider";
import { Navigation } from "@/app/(main)/_componexts/navigation";

import { redirect } from "next/navigation";
import { REFRESH_TOKEN } from "@/app/utils/fileds";
import { refreshToken, UserURI } from "@/app/services/user/userServers";
import { SearchCommand } from "@/components/search-command";

const MainLayout = ({ children }: { children: React.ReactNode }) => {
    const { isAuth, setIsAuth, isLoading, setLoading } = useContext(AuthContext);
    // FIX: Authentication requires server-side refactoring.
    function Auth() {
        const _refreshToken = localStorage.getItem(REFRESH_TOKEN);
        if (_refreshToken !== null) {
            refreshToken(_refreshToken)
                .then((accestToken) => {
                    setLoading(false);
                    setIsAuth({ accessToken: accestToken, refreshToken: _refreshToken });
                })
                .finally(() => setLoading(false));
            return;
        }
        GET({
            url: UserURI.isAuth,
            options: {
                method: "GET",
                mode: "cors",
                credentials: "include",
            },
            timeout: 9999999,
        }).then((resp) => {
            if (resp.ok) {
                resp.json()
                    .then((date) => {
                        setLoading(false);
                        setIsAuth(date as AuthPopos);
                        localStorage.setItem(REFRESH_TOKEN, date.refreshToken);
                    })
                    .finally(() => setLoading(false));
            }
        });
    }

    useEffect(() => {
        if (isLoading) {
            Auth();
        }
    }, [isLoading]);
    if (isLoading) {
        return (
            <div>
                <Spinner size="lg" />
            </div>
        );
    }

    if (!isAuth) {
        return redirect("/");
    }
    return (
        <div className="h-full flex dark:bg-[#1F1F1F]">
            <Navigation />
            <main className="flex-1 h-full overflow-y-auto">
                <SearchCommand />
                {children}
            </main>
        </div>
    );
};

export default MainLayout;