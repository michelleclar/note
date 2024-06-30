"use client";

import { ElementRef, useContext, useRef, useState } from "react";
import { Button } from "@/components/ui/button";
import TextareaAutosize from "react-textarea-autosize";
import { IconPicker } from "./icon-picker";
import { ImageIcon, Smile, X } from "lucide-react";
import { DocumentProps, removeIcon, updateById } from "@/app/services/documents/documentsService";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { useCoverImage } from "@/hooks/use-cover-image";

interface ToolbarProps {
    initialData: DocumentProps;
    preview?: boolean;
}
// NOTE: change icon need reload
export const Toolbar = ({ initialData, preview }: ToolbarProps) => {
    const inputRef = useRef<ElementRef<"textarea">>(null);

    const [isEditing, setIsEditing] = useState(false);
    const [value, setValue] = useState(initialData.title);

    const { isAuth } = useContext(AuthContext);

    const { setCount } = useContext(ClientContext);
    const coverImage = useCoverImage();

    const enableInput = () => {
        if (preview) return;

        setIsEditing(true);
        setTimeout(() => {
            setValue(initialData.title);
            inputRef.current?.focus();
        }, 0);
    };

    const disableInput = () => setIsEditing(false);
    // TODO: complete input update doc
    const onInput = (value: string) => {
        setValue(value);
    };

    const onKeyDown = (event: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if (event.key === "Enter") {
            event.preventDefault();
            disableInput();
            updateById({
                accentToken: isAuth.accessToken,
                doc: { id: initialData.id, title: value || "Untitled" },
            }).then((resp) => {
                if (resp.ok) setCount((v) => v + 1);
            });
        }
    };

    const onIconSelect = (icon: string) => {
        updateById({
            accentToken: isAuth.accessToken,
            doc: { id: initialData.id, icon: icon, title: "" },
        });
    };

    const onRemoveIcon = () => {
        removeIcon(isAuth.accessToken, initialData.id);
    };
    return (
        <div className="group relative pl-[54px]">
            {!!initialData.icon && !preview && (
                <div className="group/icon flex items-center gap-x-2 pt-6">
                    <IconPicker onChange={onIconSelect}>
                        <p className="text-6xl transition hover:opacity-75">{initialData.icon}</p>
                    </IconPicker>
                    <Button
                        onClick={onRemoveIcon}
                        className="rounded-full text-xs text-muted-foreground opacity-0 transition group-hover/icon:opacity-100"
                        variant="outline"
                        size="icon"
                    >
                        <X className="h-4 w-4" />
                    </Button>
                </div>
            )}
            {!!initialData.icon && preview && <p className="pt-6 text-6xl">{initialData.icon}</p>}
            <div className="flex items-center gap-x-1 py-2 opacity-0 group-hover:opacity-100">
                {!initialData.icon && !preview && (
                    <IconPicker asChild onChange={onIconSelect}>
                        <Button className="text-xs text-muted-foreground" variant="outline" size="sm">
                            <Smile className="mr-2 h-4 w-4" />
                            Add icon
                        </Button>
                    </IconPicker>
                )}
                {!initialData.coverImage && !preview && (
                    <Button
                        onClick={coverImage.onOpen}
                        className="text-xs text-muted-foreground"
                        variant="outline"
                        size="sm"
                    >
                        <ImageIcon className="mr-2 h-4 w-4" />
                        Add Cover
                    </Button>
                )}
            </div>
            {isEditing && !preview ? (
                <TextareaAutosize
                    ref={inputRef}
                    spellCheck="false"
                    onBlur={disableInput}
                    onKeyDown={onKeyDown}
                    value={value}
                    onChange={(e) => onInput(e.target.value)}
                    className="resize-none break-words bg-transparent text-5xl font-bold text-[#3F3F3F] outline-none dark:text-[#CFCFCF]"
                />
            ) : (
                <div
                    onClick={enableInput}
                    className="break-words pb-[11.5px] text-5xl font-bold  text-[#3F3F3F] outline-none dark:text-[#CFCFCF]"
                >
                    {initialData.title}
                </div>
            )}
        </div>
    );
};