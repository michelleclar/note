"use client";

import { DocumentProps, updateById } from "@/app/services/documents/documentsService";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Skeleton } from "@/components/ui/skeleton";
import { ChangeEvent, useContext, useRef, useState } from "react";

interface TitleProps {
    initialData: DocumentProps;
}

export const Title = ({ initialData }: TitleProps) => {
    const inputRef = useRef<HTMLInputElement>(null);

    const [title, setTitle] = useState<string>(initialData.title || "Untitled");
    const [isEditing, setIsEditing] = useState<boolean>(false);

    const enableInput = () => {
        setTitle(initialData.title);
        setIsEditing(true);
        setTimeout(() => {
            inputRef.current?.focus();
            inputRef.current?.setSelectionRange(0, inputRef.current.value.length);
        }, 0);
    };

    const disabledInput = () => {
        setIsEditing(false);
    };

    const { isAuth } = useContext(AuthContext);
    const onChange = (event: ChangeEvent<HTMLInputElement>) => {
        setTitle(event.target.value);
    };

    const { setCount } = useContext(ClientContext);

    const onKeyDown = (event: React.KeyboardEvent) => {
        if (event.key === "Enter") {
            disabledInput();
            updateById({
                accentToken: isAuth.accessToken,
                doc: { id: initialData.id, title: title || "Untitled" },
            }).then((resp) => {
                if (resp.ok) setCount((v) => v + 1);
            });
        }
    };

    return (
        <div className="flex items-center gap-x-1">
            {!!initialData.icon && <p>{initialData.icon}</p>}
            {isEditing ? (
                <Input
                    ref={inputRef}
                    onClick={enableInput}
                    onBlur={disabledInput}
                    onChange={onChange}
                    onKeyDown={onKeyDown}
                    value={title}
                    className="h-7 px-2 focus-visible:ring-transparent"
                />
            ) : (
                <Button onClick={enableInput} variant="ghost" size="sm" className="h-auto p-1 font-normal">
                    <span className="truncate">{initialData?.title}</span>
                </Button>
            )}
        </div>
    );
};

Title.Skeleton = function TitleSkeleton() {
    return <Skeleton className="h-6 w-20 rounded-md" />;
};