"use client";

import { cn } from "@/lib/utils";
import Image from "next/image";
import { Button } from "@/components/ui/button";
import { ImageIcon, X } from "lucide-react";
import { useCoverImage } from "@/hooks/use-cover-image";
import { useParams } from "next/navigation";
import { useEdgeStore } from "@/lib/edgestore";
import { Skeleton } from "./ui/skeleton";
import { removeCoverImage } from "@/app/services/documents/documentsService";
import { useContext } from "react";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";

interface CoverImageProps {
    url?: string;
    preview?: boolean;
}

export const Cover = ({ url, preview }: CoverImageProps) => {
    const { edgestore } = useEdgeStore();

    const params = useParams();
    const coverImage = useCoverImage();
    const { isAuth } = useContext(AuthContext);
    const { setCount } = useContext(ClientContext);
    const onRemove = async () => {
        if (url) {
            await edgestore.publicFiles.delete({
                url: url,
            });
        }
        removeCoverImage(isAuth.accessToken, Number(params.documentId)).then((resp) => {
            if (resp.ok) {
                setCount((v) => v + 1);
            }
        });
    };

    return (
        <div className={cn("group relative h-[35vh] w-full", !url && "h-[12vh]", url && "bg-muted")}>
            {!!url && <Image src={url} fill alt="cover" className="object-cover" priority />}
            {url && !preview && (
                <div className="absolute bottom-5 right-5 flex items-center gap-x-2 opacity-0 group-hover:opacity-100">
                    <Button
                        onClick={() => coverImage.onReplace(url)}
                        className="text-xs text-muted-foreground"
                        variant="outline"
                        size="sm"
                    >
                        <ImageIcon className="mr-2 h-4 w-4" />
                        Change cover
                    </Button>
                    <Button onClick={onRemove} className="text-xs text-muted-foreground" variant="outline" size="sm">
                        <X className="mr-2 h-4 w-4" />
                        Remove
                    </Button>
                </div>
            )}
        </div>
    );
};

Cover.Skeleton = function CoverSkeleton() {
    return <Skeleton className="h-[12vh] w-full" />;
};