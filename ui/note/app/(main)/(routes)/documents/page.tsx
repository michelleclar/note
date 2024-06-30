"use client";
import { createDocument } from "@/app/services/documents/documentsService";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { Button } from "@/components/ui/button";
// NOTE replace server
import { PlusCircle } from "lucide-react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useContext } from "react";

import { toast } from "sonner";

const DocumentPage = () => {
    const router = useRouter();
    const auth = useContext(AuthContext);
    const { user } = auth;
    // NOTE: create new doc by title is "Untitled"

    const { setCount } = useContext(ClientContext);
    const onCreate = () => {
        const promise = createDocument({ id: 0, title: "Untitled" }, auth).then((documentId) => {
            router.push(`/documents/${documentId}`);
        });
        toast.promise(promise, {
            loading: "Creating a new note...",
            success: "New note create!",
            error: "Failed to create a new note.",
        });

        setCount((current) => current + 1);
    };

    return (
        <div className="h-full flex flex-col items-center justify-center space-y-4">
            <Image
                src="/empty.png"
                height="300"
                width="300"
                alt="Empty"
                className="h-auto dark:hidden"
            />
            <Image src="/empty-dark.png" height="300" width="300" alt="Empty" className="hidden dark:block h-auto" />
            <h2>Welcome to {user?.username}&apos;s Notion</h2>
            <Button onClick={onCreate}>
                <PlusCircle className="h-4 w-4 mr-2" />
                Create a note
            </Button>
        </div>
    );
};

export default DocumentPage;