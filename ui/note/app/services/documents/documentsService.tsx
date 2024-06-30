import { GET, POST } from "@/app/utils/fetch";
import { REFRESH_TOKEN } from "@/app/utils/fileds";
import { AuthContextType } from "@/components/providers/notion-provider";
import { refreshToken } from "@/app/services/user/userServers";
export type DocumentProps = {
    id: number;
    title: string;
    userId?: number;
    isArchive?: boolean;
    parentDocumentId?: number | null;
    content?: string;
    coverImage?: string;
    icon?: string;
    isPublished?: boolean;
    children?: DocumentProps[];
};

const host = process.env.NEXT_PUBLIC_HOST + "/documents";
const CREATE_DOC = host + "/create";
const SHOW_ROOT_OR_CHILD_DOC_NOT_ARCHIVE = host + "/showRootOrChildDocumentsNotArchive";
const SHOW_IS_ARCHIVE = host + "/showDocumentsIsArchive";
const SHOW_NOT_ARCHIVE = host + "/showDocumentsNotArchive";
const ARCHIVE = host + "/archive";
const RECOVER = host + "/recover";
const REMOVE = host + "/remove";
const REMOVE_ICON = host + "/removeIcon";
const REMOVE_COVER_IMAGE = host + "/removeCoverImage";
const FIND_ID = host + "/findById";
const UPDATE_ID = host + "/updateById";

export async function createDocument(args: DocumentProps, auth: AuthContextType): Promise<number> {
    const { isAuth, setIsAuth } = auth;
    if (!isAuth) {
        const accessToken = await refreshToken(localStorage.getItem(REFRESH_TOKEN));
        setIsAuth({ accessToken: accessToken, refreshToken: localStorage.getItem(REFRESH_TOKEN)! });
    }

    const resp = await POST({
        url: CREATE_DOC,
        options: {
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json",
                Authorization: `Bearer ${isAuth.accessToken}`,
            },
        },
        data: args,
    });
    const text = await resp.text();

    return Number(text);
}

// NOTE: parentDocumentId is 0 return 'root'
export async function showRootOrChildDocumentsNotArchive(
    accentToken: string,
    parentDocumentId: number,
): Promise<DocumentProps[]> {
    const resp = await GET({
        url: SHOW_ROOT_OR_CHILD_DOC_NOT_ARCHIVE + "/" + parentDocumentId,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
    return resp.json();
}
export async function showDocumentsNotArchivee(accentToken: string): Promise<DocumentProps[]> {
    const resp = await GET({
        url: SHOW_NOT_ARCHIVE,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
    return resp.json();
}
export async function showDocumentsIsArchivee(accentToken: string): Promise<DocumentProps[]> {
    const resp = await GET({
        url: SHOW_IS_ARCHIVE,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
    return resp.json();
}

export async function archiveDocument(accentToken: string, documentId: number) {
    return await GET({
        url: ARCHIVE + "/" + documentId,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
}
export async function recoverDocument(accentToken: string, documentId: number): Promise<void> {
    await GET({
        url: RECOVER + "/" + documentId,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
}

export async function removeDocument(accentToken: string, documentId: number) {
    await GET({
        url: REMOVE + "/" + documentId,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
}
export async function removeIcon(accentToken: string, documentId: number) {
    await GET({
        url: REMOVE_ICON + "/" + documentId,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
}
export async function removeCoverImage(accentToken: string, documentId: number) {
    return await GET({
        url: REMOVE_COVER_IMAGE + "/" + documentId,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
}
export async function findById(accentToken: string, documentId: number): Promise<DocumentProps> {
    const resp = await GET({
        url: FIND_ID + "/" + documentId,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
            },
        },
    });
    if (resp.ok) {
        return resp.json();
    }
    throw Error("document not exist");
}
export async function updateById({ accentToken, doc }: { accentToken: string; doc: DocumentProps }) {
    return await POST({
        url: UPDATE_ID,
        options: {
            headers: {
                Authorization: `Bearer ${accentToken}`,
                Accept: "application/json",

                "Content-Type": "application/json",
            },
        },
        data: doc,
    });
}