"use client";

import { recoverDocument, removeDocument } from "@/app/services/documents/documentsService";
import { DOCUMENTS } from "@/app/utils/fileds";
import { ConfirmModal } from "@/components/modals/confirm-modal";
import { AuthContext } from "@/components/providers/notion-provider";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";
import { useContext } from "react";
import { toast } from "sonner";

interface BannerProps {
    documentId: number;
}

export const Banner = ({ documentId }: BannerProps) => {
    const router = useRouter();
    const { isAuth } = useContext(AuthContext);
    const onRemove = () => {
        const promise = removeDocument(isAuth.accessToken, documentId);

        toast.promise(promise, {
            loading: "Deleting note...",
            success: "Note Deleted!",
            error: "Failed to delete note.",
        });

        router.push(`/${DOCUMENTS}`);
    };

    const onRestore = () => {
        const promise = recoverDocument(isAuth.accessToken, documentId);

        toast.promise(promise, {
            loading: "Restoring note...",
            success: "Note restored!",
            error: "Failed to restore note.",
        });
    };

    return (
        <div className="flex w-full items-center justify-center gap-x-2 bg-rose-500 p-2 text-center text-sm text-white">
            <p>
                This page is in the <span className="font-bold">Trash.</span>
            </p>
            <Button
                size="sm"
                onClick={onRestore}
                variant="outline"
                // className="border-white bg-transparent hover:bg-primary/5 text-white hover:text-white p-1 px-2 h-auto font-normal hover:text-rose-500"
                className="h-auto border-white bg-transparent p-1 px-2 font-normal text-white transition hover:bg-white hover:text-rose-500"
            >
                Restore page
            </Button>
            <ConfirmModal onConfirm={onRemove}>
                <Button
                    size="sm"
                    variant="outline"
                    className="h-auto border-white bg-transparent p-1 px-2 font-normal text-white transition hover:bg-white hover:text-rose-500"
                >
                    Delete forever
                </Button>
            </ConfirmModal>
        </div>
    );
};