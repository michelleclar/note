// "use client";

import { MenuIcon } from "lucide-react";
import { useParams } from "next/navigation";
import { Title } from "./title";
import { useContext, useEffect, useState } from "react";
import { DocumentProps, findById } from "@/app/services/documents/documentsService";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { Banner } from "@/app/(main)/_componexts/banner";
import { Menu } from "@/app/(main)/_componexts/menu";
import { Publish } from "@/app/(main)/_componexts/publish";

interface NavbarProps {
    isCollapsed: boolean;
    onResetWidth: () => void;
}

export const Navbar = ({ isCollapsed, onResetWidth }: NavbarProps) => {
    const params = useParams();

    const { isAuth } = useContext(AuthContext);
    const [document, setDocument] = useState<DocumentProps>();

    const { fetchCount } = useContext(ClientContext);
    useEffect(() => {
        findById(isAuth.accessToken, Number(params.documentId)).then((r) => {
            setDocument(r);
        });
    }, [params.documentId, fetchCount]);

    if (document === undefined) {
        return (
            <nav className="flex w-full items-center justify-between bg-background px-3 py-2 dark:bg-[#1F1F1F]">
                <Title.Skeleton />
                <div className="flex items-center gap-x-2 ">
                    <Menu.Skeleton />
                </div>
            </nav>
        );
    }

    if (document === null) {
        return null;
    }

    return (
        <>
            <nav className="flex w-full items-center gap-x-2 bg-background px-3 py-2 dark:bg-[#1F1F1F]">
                {isCollapsed && (
                    <button aria-label="Menu">
                        <MenuIcon onClick={onResetWidth} className="h-6 w-6 text-muted-foreground" />
                    </button>
                )}
                <div className="flex w-full items-center justify-between">
                    <Title initialData={document} />
                    <div className="flex items-center gap-x-2">
                        <Publish initialData={document}/>
                        <Menu documentId={document.id} />
                    </div>
                </div>
            </nav>
            {document.isArchive && <Banner documentId={document.id} />}
        </>
    );
};