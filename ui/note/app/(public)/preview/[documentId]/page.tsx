"use client";

import dynamic from "next/dynamic";
import { useContext, useEffect, useMemo, useState } from "react";

import { Skeleton } from "@/components/ui/skeleton";

import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { DocumentProps, findById, updateById } from "@/app/services/documents/documentsService";
import { Toolbar } from "@/components/toolbar";
import { Cover } from "@/components/cover";
import { refreshToken } from "@/app/services/user/userServers";
import { REFRESH_TOKEN } from "@/app/utils/fileds";

interface DocumentIdPageProps {
    params: {
        documentId: number;
    };
}
const DocumentIdPage = ({ params }: DocumentIdPageProps) => {
    const Editor = useMemo(() => dynamic(() => import("@/components/editor"), { ssr: false }), []);
    const { isAuth } = useContext(AuthContext);
    const [document, setDocument] = useState<DocumentProps>();

    const { fetchCount } = useContext(ClientContext);
    const { setIsAuth } = useContext(AuthContext);

    useEffect(() => {
        const _refreshToken = localStorage.getItem(REFRESH_TOKEN);
        if (!isAuth && _refreshToken !== null) {
            refreshToken(_refreshToken).then((accestToken) => {
                setIsAuth({ accessToken: accestToken, refreshToken: _refreshToken });
            });
        }
        findById(isAuth.accessToken, Number(params.documentId)).then((r) => {
            setDocument(r);
        });
    }, [params.documentId, fetchCount]);

    const onChange = (content: string) => {
        console.log(content);

        updateById({
            accentToken: isAuth.accessToken,
            doc: { id: Number(params.documentId), content: content, title: "" },
        });
    };

    if (document === undefined) {
        return (
            <div>
                <Cover.Skeleton />
                <div className="mx-auto mt-10 md:max-w-3xl lg:max-w-4xl">
                    <div className="space-y-4 pl-8 pt-4">
                        <Skeleton className="h-14 w-1/2" />
                        <Skeleton className="h-4 w-4/5" />
                        <Skeleton className="h-4 w-2/5" />
                        <Skeleton className="h-4 w-3/5" />
                    </div>
                </div>
            </div>
        );
    }

    if (document === null) {
        return <div>Not found</div>;
    }
    return (
        <div className="pb-40">
            <Cover preview url={document.coverImage} />
            <div className="mx-auto md:max-w-3xl lg:md-max-w-4xl">
                <Toolbar preview initialData={document} />
                <Editor onChange={onChange} initialContent={document.content} editable={false} />
            </div>
        </div>
    );
};
export default DocumentIdPage;