"use client";
import { DocumentProps } from "@/app/services/documents/documentsService";
import { User } from "@/app/services/user/userServers";
import React, { createContext, ReactNode, Dispatch, SetStateAction, useState, useContext, useRef } from "react";

//FIX: AuthContext ClientContext
export type AuthPopos = {
    accessToken: string;
    refreshToken: string;
};
export interface AuthContextType {
    isLoading: boolean;
    isAuth: AuthPopos;
    setLoading: Dispatch<SetStateAction<boolean>>;
    setIsAuth: Dispatch<SetStateAction<AuthPopos>>;
    user: User;
    setUser: Dispatch<SetStateAction<User>>;
}

interface ClientContextType {
    fetchCount: number;

    setCount: Dispatch<SetStateAction<number>>;
}

export const AuthContext = createContext<AuthContextType>(null as any);
export const ClientContext = createContext<ClientContextType>(null as any);

export const AuthProviders = ({ children }: { children: ReactNode }) => {
    const [isAuth, setIsAuth] = useState<AuthPopos>(null as any);
    const [isLoading, setLoading] = useState<boolean>(true);
    const [user, setUser] = useState<User>(null as any);
    const auth = useRef<AuthPopos>();
    const [fetchCount, setCount] = useState<number>(0);

    return (
        <AuthContext.Provider value={{ isAuth, isLoading, setLoading, setIsAuth, user, setUser }}>
            <ClientContext.Provider value={{ fetchCount, setCount }}>{children}</ClientContext.Provider>
        </AuthContext.Provider>
    );
};

export const useContextAuth = () => {
    const auth = useContext(AuthContext);

    if (auth === undefined) {
        throw new Error("auth is empty");
    }
    // get({ uri: checkServerHealthURI }).then((resp) => {
    //     if (resp.ok) auth.setLoading(false);
    // });
    return auth;
};