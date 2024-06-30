"use client";

import { Dialog, DialogContent, DialogHeader } from "@/components/ui/dialog";
import { useCoverImage } from "@/hooks/use-cover-image";
import { SingleImageDropzone } from "@/components/single-image-dropzone";
import { useContext, useState } from "react";
import { useEdgeStore } from "@/lib/edgestore";
import { useParams } from "next/navigation";
import { updateById } from "@/app/services/documents/documentsService";
import { AuthContext, ClientContext } from "@/components/providers/notion-provider";

export const CoverImageModal = () => {
    const params = useParams();

    const [file, setFile] = useState<File>();
    const [isSubmitting, setIsSubmitting] = useState(false);

    const coverImage = useCoverImage();
    const { edgestore } = useEdgeStore();

    const onClose = () => {
        setFile(void 0);
        setIsSubmitting(false);
        coverImage.onClose();
    };
    const { isAuth } = useContext(AuthContext);
    const { setCount } = useContext(ClientContext);
    const onChange = async (file?: File) => {
        if (file) {
            setIsSubmitting(true);
            setFile(file);

            const res = await edgestore.publicFiles.upload({
                file,
                options: {
                    replaceTargetUrl: coverImage.url,
                },
            });
            await updateById({
                accentToken: isAuth.accessToken,
                doc: { id: Number(params.documentId), title: "", coverImage: res.url },
            }).then((resp) => {
                if (resp.ok) {
                    setCount((v) => v + 1);
                }
            });

            onClose();
        }
    };

    return (
        <Dialog open={coverImage.isOpen} onOpenChange={coverImage.onClose}>
            <DialogContent>
                <DialogHeader>
                    <h2 className="text-center text-lg font-semibold">Cover Image</h2>
                </DialogHeader>
                <SingleImageDropzone
                    className="w-full outline-none"
                    disabled={isSubmitting}
                    value={file}
                    onChange={onChange}
                />
            </DialogContent>
        </Dialog>
    );
};