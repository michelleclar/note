"use client";

import {
    DocumentProps,
    recoverDocument,
    removeDocument,
    showDocumentsIsArchivee,
} from "@/app/services/documents/documentsService";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { useParams, useRouter } from "next/navigation";
import { useContext, useEffect, useState } from "react";
import { DOCUMENTS } from "@/app/utils/fileds";
import { toast } from "sonner";
import { Spinner } from "@/components/spinner";
import { Search, Trash, Undo } from "lucide-react";
import { Input } from "@/components/ui/input";
import { ConfirmModal } from "@/components/modals/confirm-modal";

export const TrashBox = () => {
    const router = useRouter();
    const params = useParams();

    const { isAuth } = useContext(AuthContext);
    const [search, setSearch] = useState("");

    const [documents, setDocuments] = useState<DocumentProps[]>();

    const { fetchCount, setCount } = useContext(ClientContext);
    useEffect(() => {
        showDocumentsIsArchivee(isAuth.accessToken).then((r) => {
            setDocuments(r);
        });
    }, [fetchCount]);
    const filteredDocuments = documents?.filter((doc) => {
        return doc.title.toLowerCase().includes(search.toLowerCase());
    });
    const onClick = (docId: number) => {
        router.push(`/${DOCUMENTS}/${docId}`);
    };
    const onRestore = (event: React.MouseEvent<HTMLDivElement, MouseEvent>, documentId: number) => {
        event.stopPropagation();
        const primise = recoverDocument(isAuth.accessToken, documentId);
        toast.promise(primise, {
            loading: "Restoring note...",
            success: "Note restored!",
            error: "Failed to restore note.",
        });

        setCount((current) => current + 1);
    };
    const onRemove = (documentId: number) => {
        const primise = removeDocument(isAuth.accessToken, documentId);
        toast.promise(primise, {
            loading: "Deleting note...",
            success: "Note delete!",
            error: "Failed to delete note.",
        });

        if (Number(params.documentId) === documentId) {
            router.push(`/${DOCUMENTS}`);
        }
        setCount((current) => current + 1);
    };
    if (documents === undefined) {
        return (
            <div className="h-full flex items-center justify-center p-4">
                <Spinner size="lg" />
            </div>
        );
    }

    return (
        <div className="text-sm">
            <div className="flex items-center gap-x-1 p-2">
                <Search className="h-4 w-4" />
                <Input
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="h-7 px-2 focus-visible:ring-transparent bg-secondary"
                    placeholder="Filter by page title..."
                />
            </div>
            <div className="mt-2 px-1 pb-1">
                <p className="hidden last:block text-xs text-center text-muted-foreground pb-2">No documents found</p>
                {filteredDocuments?.map((document) => (
                    <div
                        key={document.id}
                        role="button"
                        onClick={() => onClick(document.id)}
                        className="text-sm rounded-sm w-full hover:bg-primary/5 flex items-center text-primary justify-between"
                    >
                        <span className="truncate pl-2">{document.title}</span>
                        <div className="flex items-center">
                            <div
                                onClick={(e) => onRestore(e, document.id)}
                                role="button"
                                className="rounded-sm p-2 hover:bg-neutral-200 dark:hover:bg-neutral-600"
                            >
                                <Undo className="h-4 w-4 text-muted-foreground" />
                            </div>
                            <ConfirmModal onConfirm={() => onRemove(document.id)}>
                                <div
                                    role="button"
                                    className="rounded-sm p-2 hover:bg-neutral-200 dark:hover:bg-neutral-600"
                                >
                                    <Trash className="h-4 w-4 text-muted-foreground" />
                                </div>
                            </ConfirmModal>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};