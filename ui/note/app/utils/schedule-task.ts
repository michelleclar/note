import { useRef } from "react";

export class DelayTime {
    time: number;
    static DEFAULT(): DelayTime {
        return new DelayTime(5, "SECONDS");
    }
    duration: "DAYS" | "HOURS" | "MINUTES" | "SECONDS" | "MILLIS" | "NANOT";
    constructor(time: number, duration: "DAYS" | "HOURS" | "MINUTES" | "SECONDS" | "MILLIS" | "NANOT") {
        this.time = time;
        this.duration = duration;
    }

    getTime() {
        switch (this.duration) {
            case "NANOT":
                return this.time / 1000;
            case "MILLIS":
                return this.time;
            case "SECONDS":
                return this.time * 1000;
            case "MINUTES":
                return this.time * 1000 * 60;
            case "MINUTES":
                return this.time * 1000 * 60 * 60;
        }
    }
    static of(time: number, duration: "DAYS" | "HOURS" | "MINUTES" | "SECONDS" | "MILLIS" | "NANOT") {
        return new DelayTime(time, duration);
    }
}

export interface DelayTaskProps {
    task: () => void;
    delayTime?: DelayTime;
    repeat?: number;
    timer?: DelayTime;
}
export const DelayTask = ({
    task,
    delayTime = DelayTime.DEFAULT(),
    repeat = 1,
    timer = DelayTime.DEFAULT(),
}: DelayTaskProps): number|null => {
    let executionCount = 0;
    let r: number | null = null;
    const delayedStart = window.setTimeout(() => {
        const interval = window.setInterval(() => {
            try {
                task();
                executionCount++;
            } catch (error) {
                if (error instanceof Error) console.log(error.message);
                executionCount++;
            }
            if (repeat !== -1 && executionCount >= repeat && interval !== null) clearInterval(interval);
        }, timer.getTime());
        r = interval;
        clearTimeout(delayedStart);
    }, delayTime.getTime());
    return r;
};

export const TimerTask = ({ task, repeat = 1, timer = DelayTime.DEFAULT() }: DelayTaskProps) => {
    let executionCount = 0;

    const intervalRef = useRef(0);
    const interval = window.setInterval(() => {
        try {
            task();
            executionCount++;
        } catch (error) {
            if (error instanceof Error) console.debug(error.message);
            executionCount++;
        }
        if (repeat !== -1 && executionCount >= repeat && interval !== null) clearInterval(interval);
    }, timer.getTime());
    intervalRef.current = interval;
    return intervalRef;
};

//     const intervalRef = useRef<number | null>(null);
//     const [executionCount, setExecutionCount] = useState(0);
//
//     useEffect(() => {
//         const delayedStart = window.setTimeout(() => {
//             intervalRef.current = window.setInterval(() => {
//                 task();
//                 setExecutionCount((prevCount) => prevCount + 1);
//             }, timer);
//         }, delayTime);
//         console.log("1");
//         // 清理函数
//         return () => {
//             if (intervalRef.current !== null) {
//                 clearInterval(intervalRef.current);
//                 intervalRef.current = null;
//             }
//             clearTimeout(delayedStart);
//         };
//     }, [task, delayTime, timer]);
//
//     useEffect(() => {
//         console.log("2");
//         if (repeat !== -1 && executionCount >= repeat) {
//             if (intervalRef.current !== null) {
//                 clearInterval(intervalRef.current);
//                 intervalRef.current = null;
//             }
//         }
//     }, [executionCount, repeat]);
//
//     return null;
// };
// export const DelayTask: React.FC<DelayTaskProps> = ({ task, delayTime = 5000, repeat = 1, timer = 5000 }) => {