"use client";

import { useRouter } from "next/navigation";
import { toast } from "sonner";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { MoreHorizontal, Trash } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { archiveDocument } from "@/app/services/documents/documentsService";
import { useContext } from "react";
import { AuthContext } from "@/components/providers/notion-provider";

interface MenuProps {
    documentId: number;
}

export const Menu = ({ documentId }: MenuProps) => {
    const router = useRouter();

    const { isAuth, user } = useContext(AuthContext);
    const onArchive = () => {
        const promise = archiveDocument(isAuth.accessToken, documentId);

        toast.promise(promise, {
            loading: "Moving to trash...",
            success: "Note moved to trash!",
            error: "Failed to archive note.",
        });

        router.push("/documents");
    };

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button size="sm" variant="ghost">
                    <MoreHorizontal className="h-4 w-4" />
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-60" align="end" alignOffset={8} forceMount>
                <DropdownMenuItem onClick={onArchive}>
                    <Trash className="mr-2 h-4 w-4" />
                    Delete
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <div className="p-2 text-xs text-muted-foreground">Last edited by {user?.username}</div>
            </DropdownMenuContent>
        </DropdownMenu>
    );
};

Menu.Skeleton = function MenuSkeleton() {
    return <Skeleton className="h-8 w-8" />;
};