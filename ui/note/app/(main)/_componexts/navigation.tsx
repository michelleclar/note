"use client";

import { UserItem } from "./user-item";
import { cn } from "@/lib/utils";
import { ChevronsLeft, MenuIcon, Plus, PlusCircle, Search, Settings, Trash } from "lucide-react";
import { useParams, usePathname } from "next/navigation";
import { useRef, ElementRef, useState, useEffect, useContext } from "react";
import { useMediaQuery } from "usehooks-ts";

import { AuthContext, ClientContext } from "@/components/providers/notion-provider";
import { Item } from "@/app/(main)/_componexts/item";
import { toast } from "sonner";
import { DocumentList } from "@/app/(main)/_componexts/document-list";
import { createDocument } from "@/app/services/documents/documentsService";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { TrashBox } from "@/app/(main)/_componexts/trash-box";
import { useSearch } from "@/hooks/use-search";
import { useSettings } from "@/hooks/use-setting";
import { Navbar } from "@/app/(main)/_componexts/navbar";
import { useRouter } from "next/navigation";

export const Navigation = () => {
    const router = useRouter();
    //get current path
    const pathname = usePathname();
    //detect isMobile
    const isMobile = useMediaQuery("(max-width: 768px)");
    //track isResizing
    const isResizingRef = useRef(false);
    //side
    const sidebarRef = useRef<ElementRef<"aside">>(null);
    //navbar
    const navbarRef = useRef<ElementRef<"div">>(null);
    //control side isResizing
    const [isResetting, setIsResetting] = useState(false);
    //side isCollapsed
    const [isCollapsed, setIsCollapsed] = useState(isMobile);
    const params = useParams();
    useEffect(() => {
        if (isMobile) {
            //fold side
            collapse();
        } else {
            //restore side
            resetWidth();
        }
    }, [isMobile]);
    useEffect(() => {
        if (isMobile) {
            collapse();
        }
    }, [pathname, isMobile]);
    const handleMouseDown = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        event.preventDefault();
        event.stopPropagation();
        isResizingRef.current = true;
        document.addEventListener("mousemove", handleMouseMove);
        document.addEventListener("mouseup", handleMouseUp);
    };

    const handleMouseMove = (e: MouseEvent) => {
        if (!isResizingRef.current) return;
        let newWidth = e.clientX;
        if (newWidth < 240) newWidth = 240;
        if (newWidth > 480) newWidth = 480;
        if (sidebarRef.current && navbarRef.current) {
            sidebarRef.current.style.width = `${newWidth}px`;
            navbarRef.current.style.setProperty("left", `${newWidth}px`);
            navbarRef.current.style.setProperty("width", `calc(100%-${newWidth}px)`);
        }
    };
    const handleMouseUp = () => {
        isResizingRef.current = false;
        document.removeEventListener("mousemove", handleMouseMove);
        document.removeEventListener("mouseup", handleMouseUp);
    };

    const resetWidth = () => {
        if (sidebarRef.current && navbarRef.current) {
            setIsCollapsed(false);
            setIsResetting(true);
            sidebarRef.current.style.width = isMobile ? "100%" : "240px";
            navbarRef.current.style.setProperty("width", isMobile ? "0" : "calc(100%-240px)");
            navbarRef.current.style.setProperty("left", isMobile ? "100" : "240px");
            setTimeout(() => setIsResetting(false), 300);
        }
    };
    const collapse = () => {
        if (sidebarRef.current && navbarRef.current) {
            setIsCollapsed(true);
            setIsResetting(true);
            sidebarRef.current.style.width = "0";
            navbarRef.current.style.setProperty("width", "100%");
            navbarRef.current.style.setProperty("left", "0");

            setTimeout(() => setIsResetting(false), 300);
        }
    };

    const auth = useContext(AuthContext);

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
    const search = useSearch();
    const settings = useSettings();

    return (
        <>
            <aside
                ref={sidebarRef}
                className={cn(
                    "group/sidebar h-full bg-secondary overflow-y-auto relative flex w-60 flex-col z-[50]",
                    isResetting && "transition-all ease-in-out duration-300",
                    isMobile && "w-0",
                )}
            >
                <div
                    onClick={collapse}
                    role="button"
                    className={cn(
                        "h-6 w-6 text-muted-foreground rounded-sm hover:bg-neutral-300 dark:hover:bg-neutral-600 absolute top-3 right-2 opacity-0 group-hover/sidebar:opacity-100 transition",
                        isMobile && "opacity-100",
                    )}
                >
                    <ChevronsLeft className="h-6 w-6" />
                </div>
                <div>
                    <UserItem />
                    <Item label="Search" icon={Search} isSearch onClick={search.onOpen} />

                    <Item label="Settings" icon={Settings} onClick={settings.onOpen} />
                    <Item onClick={onCreate} label="New page" icon={PlusCircle} />
                </div>
                <div className="mt-4">
                    <DocumentList />
                    <Item onClick={onCreate} label="Add a page" icon={Plus} />
                    <Popover>
                        <PopoverTrigger className="w-full mt-4">
                            <Item label="Trash" icon={Trash} />
                        </PopoverTrigger>
                        <PopoverContent className="p-0 w-72" side={isMobile ? "bottom" : "right"}>
                            <TrashBox />
                        </PopoverContent>
                    </Popover>
                </div>
                <div
                    onMouseDown={handleMouseDown}
                    onClick={resetWidth}
                    className="opacity-0 group-hover/sidebar:opacity-100 transition cursor-ew-resize absolute h-full w-1 bg-primary/10 right-0 top-0"
                />
            </aside>
            <div
                ref={navbarRef}
                className={cn(
                    "absolute top-0 z-[50] left-60 w-[calc(100%-240px)]",
                    isResetting && "transition-all ease-in-out duration-300",
                    isMobile && "left-0 w-full",
                )}
            >
                {!!params.documentId ? (
                    <Navbar isCollapsed={isCollapsed} onResetWidth={resetWidth} />
                ) : (
                    <nav
                        className="bg-transparent px-3 py-2 w-full"
                    >
                        {isCollapsed && (
                            <MenuIcon onClick={resetWidth} role="button" className="h-6 w-6 text-muted-foreground" />
                        )}
                    </nav>
                )}
            </div>
        </>
    );
};