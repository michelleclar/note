"use client";

import { Item } from "@/app/(main)/_componexts/item";
import { DocumentProps, showRootOrChildDocumentsNotArchive } from "@/app/services/documents/documentsService";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { cn } from "@/lib/utils";
import { FileIcon } from "lucide-react";
import { useParams, useRouter } from "next/navigation";
import { useContext, useEffect, useState } from "react";

interface DocumentListProps {
    parentDocumentId?: number;
    level?: number;
    data?: DocumentProps[];
}
export const DocumentList = ({ parentDocumentId, level = 0 }: DocumentListProps) => {
    const { isAuth } = useContext(AuthContext);
    const params = useParams();
    const router = useRouter();
    const [expanded, setExpanded] = useState<Record<number, boolean>>({});

    const onExpand = (documentId: number) => {
        setExpanded((prevExpanded) => ({ ...prevExpanded, [documentId]: !prevExpanded[documentId] }));
    };
    const [documents, setDocuments] = useState<DocumentProps[]>();

    const { fetchCount } = useContext(ClientContext);

    useEffect(() => {
        showRootOrChildDocumentsNotArchive(
            isAuth.accessToken,
            parentDocumentId === undefined ? 0 : parentDocumentId,
        ).then((r) => {
            setDocuments(r);
        });
    }, [fetchCount]);
    const onRedirect = (documentId: number) => {
        router.push(`/documents/${documentId}`);
    };
    if (documents === void 0) {
        return (
            <>
                <Item.Skeleton level={level} />
                {level === 0 && (
                    <>
                        <Item.Skeleton level={level} />
                        <Item.Skeleton level={level} />
                    </>
                )}
            </>
        );
    }
    return (
        <>
            <p
                style={{ paddingLeft: level ? `${level * 12 + 25}px` : void 0 }}
                className={cn(
                    "hidden text-sm font-medium text-muted-foreground/80",
                    expanded && "last:block",
                    level === 0 && "hidden",
                )}
            >
                No pages inside
            </p>
            {documents.map((document) => (
                <div key={document.id}>
                    <Item
                        id={document.id}
                        onClick={() => onRedirect(document.id)}
                        label={document.title}
                        icon={FileIcon}
                        documentIcon={document.icon}
                        active={Number(params.documentId) === document.id}
                        level={level}
                        onExpand={() => onExpand(document.id)}
                        expanded={expanded[document.id]}
                    />
                    {expanded[document.id] && <DocumentList parentDocumentId={document.id} level={level + 1} />}
                </div>
            ))}
        </>
    );
};