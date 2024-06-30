"use client";
import { User } from "@/app/services/user/userServers";
import React, { createContext, ReactNode, Dispatch, SetStateAction, useState, useContext, useRef } from "react";

type LoginContextType = {
    accessToken: string;
    setAccessToken: Dispatch<SetStateAction<string>>;
    user: User | null;
    setUser: Dispatch<SetStateAction<User>>;
};

export const LoginContext = createContext<LoginContextType>(undefined as any);

export const LoginProvider = ({ children }: { children: ReactNode }) => {
    const [accessToken, setAccessToken] = useState<string>("");
    const [user, setUser] = useState<User>(null as any);
    return (
        <LoginContext.Provider value={{ accessToken, setAccessToken, user, setUser }}>{children}</LoginContext.Provider>
    );
};