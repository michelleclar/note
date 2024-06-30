"use client";

import { useContext, useEffect, useState } from "react";
import { File } from "lucide-react";
import { useRouter } from "next/navigation";

import {
    CommandDialog,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command";
import { useSearch } from "@/hooks/use-search";
import { getUser } from "@/app/services/user/userServers";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { DocumentProps, showDocumentsNotArchivee } from "@/app/services/documents/documentsService";
import { DOCUMENTS } from "@/app/utils/fileds";

export const SearchCommand = () => {
    const { isAuth, user, setUser } = useContext(AuthContext);
    const router = useRouter();

    const [isMounted, setIsMounted] = useState(false);
    const toggle = useSearch((store) => store.toggle);
    const isOpen = useSearch((store) => store.isOpen);
    const onClose = useSearch((store) => store.onClose);
    const [documents, setDocuments] = useState<DocumentProps[]>();
    const { fetchCount } = useContext(ClientContext);
    if (!user) {
        console.log("search:check getUser");

        getUser(isAuth.accessToken).then((u) => setUser(u));
    }

    useEffect(() => {
        setIsMounted(true);
    }, []);
    useEffect(() => {
        if (isOpen) {
            showDocumentsNotArchivee(isAuth.accessToken).then((r) => {
                setDocuments(r);
            });
        }
    }, [isOpen, fetchCount]);

    useEffect(() => {
        const down = (e: KeyboardEvent) => {
            if (e.key === "k" && (e.metaKey || e.ctrlKey)) {
                e.preventDefault();
                toggle();
            }
        };

        document.addEventListener("keydown", down);
        return () => document.removeEventListener("keydown", down);
    }, [toggle]);

    const onSelect = (id: string) => {
        router.push(`/${DOCUMENTS}/${id}`);
        onClose();
    };

    if (!isMounted) {
        return null;
    }

    return (
        <CommandDialog open={isOpen} onOpenChange={onClose}>
            <CommandInput placeholder={`Search ${user?.username}'s notion..`} />
            <CommandList>
                <CommandEmpty>No results found.</CommandEmpty>
                <CommandGroup heading="Documents">
                    {documents?.map((document) => (
                        <CommandItem
                            key={document.id}
                            value={`${document.id}-${document.title}`}
                            title={document.title}
                            onSelect={onSelect}
                        >
                            {document.icon ? (
                                <p className="mr-2 text-[1.125rem]">{document.icon}</p>
                            ) : (
                                <File className="mr-2 h-4 w-4" />
                            )}
                            <span>{document.title}</span>
                        </CommandItem>
                    ))}
                </CommandGroup>
            </CommandList>
        </CommandDialog>
    );
};