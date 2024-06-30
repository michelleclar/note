// "use client";

import { archiveDocument, createDocument } from "@/app/services/documents/documentsService";
import { DOCUMENTS } from "@/app/utils/fileds";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Skeleton } from "@/components/ui/skeleton";
import { cn } from "@/lib/utils";
import { ChevronDown, ChevronRight, LucideIcon, MoreHorizontal, Plus, Trash } from "lucide-react";
import { useRouter } from "next/navigation";
import { useContext} from "react";
import { toast } from "sonner";

interface ItemProps {
    id?: number;
    documentIcon?: string;
    active?: boolean;
    expanded?: boolean;
    isSearch?: boolean;
    level?: number;
    onExpand?: () => void;
    label: string;
    onClick?: () => void;
    icon: LucideIcon;
}
export const Item = ({
    id,
    label,
    onClick,
    icon: Icon,
    documentIcon,
    active,
    expanded,
    isSearch,
    level = 0,
    onExpand,
}: ItemProps) => {
    const router = useRouter();
    const ChevronIcon = expanded ? ChevronDown : ChevronRight;
    const handleExpand = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        event.stopPropagation();
        onExpand?.();
    };
    const auth = useContext(AuthContext);
    const { user, isAuth } = auth;

    const { setCount } = useContext(ClientContext);
    const onCreate = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        event.stopPropagation();
        if (!id) return;
        const promise = createDocument({ id: 0, title: "Untitled", parentDocumentId: id }, auth).then((doc) => {
            if (!expanded) {
                onExpand?.();
            }
            // router.push(`/documents/${doc}`);
        });
        toast.promise(promise, {
            loading: "Creating a new note...",
            success: "New note create!",
            error: "Failed to create a new note.",
        });
        setCount((current) => current + 1);
    };
    const onArchive = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        event.stopPropagation();
        if (!id) return;
        const promise = archiveDocument(isAuth.accessToken, id).then((resp) => {
            if (resp.ok) {
                router.push(`/${DOCUMENTS}`);
            }
        });
        toast.promise(promise, {
            loading: "Moving to trash...",
            success: "Note moved to trash!",
            error: "Failed to archive note.",
        });
        setCount((current) => current - 1);
    };

    return (
        <div
            onClick={onClick}
            role="button"
            style={{ paddingLeft: level ? `${level * 12 + 12}px` : "12px" }}
            className={cn(
                "group min-h-[27px] text-sm py-1 pr-3 w-full hover:bg-primary/5 flex items-center text-muted-foreground font-medium",
                active && "bg-primary/5 text-primary",
            )}
        >
            {!!id && (
                <div
                    role="button"
                    className="h-full rounded-sm hover:bg-neutral-300 dark:bg-neutral-600 mr-1"
                    onClick={handleExpand}
                >
                    <ChevronIcon className="h-4 w-4 shrink-0 text-muted-foreground/50" />
                </div>
            )}
            {documentIcon ? (
                <div className="shrink-0 mr-2 text-[18px]">{documentIcon}</div>
            ) : (
                <Icon className="shrink-0 h-[18px] w-[18px] mr-2 text-muted-foreground" />
            )}
            <span className="truncate">{label}</span>
            {isSearch && (
                <kbd className="ml-auto pointer-events-none inline-flex select-none items-center gap-1 rounded border bg-muted px-1.5 font-mono text-[10px] font-medium text-muted-foreground opacity-100">
                    <span className="text-xs">ctrl</span>K
                </kbd>
            )}
            {!!id && (
                <div className="ml-auto flex items-center gap-x-2">
                    <DropdownMenu>
                        <DropdownMenuTrigger onClick={(e) => e.stopPropagation()} asChild>
                            <div
                                role="button"
                                className="opacity-0 group-hover:opacity-100 h-full ml-auto rounded-sm hover:bg-neutral-300 dark:hover:bg-neutral-600"
                            >
                                <MoreHorizontal className="h-4 w-4 text-muted-foreground" />
                            </div>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent className="w-60" align="start" side="right" forceMount>
                            <DropdownMenuItem onClick={onArchive}>
                                <Trash className="h-4 w-4 mr-2" />
                                Delete
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <div className="text-xs text-muted-foreground p-2">Last edited by: {user?.username}</div>
                        </DropdownMenuContent>
                    </DropdownMenu>
                    <div
                        role="button"
                        onClick={onCreate}
                        className="opacity-0 group-hover:opacity-100 h-full ml-auto rounded-sm hover:bg-neutral-300 dark:hover:bg-neutral-600"
                    >
                        <Plus className="h-4 w-4 text-muted-foreground" />
                    </div>
                </div>
            )}
        </div>
    );
};

Item.Skeleton = function ItemSkeleont({ level }: { level?: number }) {
    return (
        <div
            style={{
                paddingLeft: level ? `${level * 12 + 25}px` : "12px",
            }}
            className="flex gap-x-2 py-[3px]"
        >
            <Skeleton className="h-4 w-4" />
            <Skeleton className="h-4 w-[30%]" />
        </div>
    );
};