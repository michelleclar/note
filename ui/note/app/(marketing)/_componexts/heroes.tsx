import Image from "next/image";

export const Heroes = () => {
    return (
        <div className="flex flex-col items-center justify-center max-w-5xl">
            <div className="flex items-center">
                <div className="relative w-[300px] h-[300px] sm:w-[350px] sm:h-[350px] md:h-[400px] md:[w-400px]">
                    <Image
                        src="/documents.png"
                        fill
                        sizes="(max-width: 768px) 100vw (small-width: 621px) 100vw"
                        className="object-contain dark:hidden"
                        alt="Doucuments"
                    />
                    <Image
                        src="/documents-dark.png"
                        fill
                        sizes="(max-width: 768px) 100vw (small-width: 621px) 100vw"
                        className="object-contain hidden dark:block"
                        alt="Doucuments"
                    />
                </div>

                <div className="relative w-[400px] h-[400px] hidden md:block">
                    <Image
                        src="/reading.png"
                        fill
                        sizes="(max-width: 768px) 100vw (small-width: 621px) 100vw"
                        className="object-contain dark:hidden"
                        alt="Reading"
                    />
                    <Image
                        src="/reading-dark.png"
                        fill
                        sizes="(max-width: 768px) 100vw (small-width: 621px) 100vw"
                        className="object-contain hidden dark:block"
                        alt="Reading"
                    />
                </div>
            </div>
        </div>
    );
};