const DefaultOptions: RequestInit = {
    mode: "cors", // no-cors, *cors, same-origin
    cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
    credentials: "same-origin", // include, *same-origin, omit
    headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        // 'Content-Type': 'application/x-www-form-urlencoded',
    },
    redirect: "follow", // manual, *follow, error
    referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
};
const getDefaultOptions: RequestInit = {
    ...DefaultOptions,
    method: "GET",
};
const postDefaultOptions: RequestInit = {
    ...DefaultOptions,
    method: "POST",
};

interface fetchProps extends RequestInit {
    url: string;
    data?: any;
    timeout?: number;
    retries?: number;
    retryDelay?: number;
    options?: RequestInit;
}

async function fetchWithTimeout(
    url: string,
    options: RequestInit,
    timeout: number,
    retries: number,
    retryDelay: number,
): Promise<Response> {
    const controller = new AbortController();
    options.signal = controller.signal;
    const fetchPromise = fetch(url, options);

    const timeoutPromise = new Promise<Response>((_, reject) =>
        setTimeout(() => {
            controller.abort();
            reject(new Error("Request timed out"));
        }, timeout),
    );

    return Promise.race([fetchPromise, timeoutPromise]).catch(async (error) => {
        if (retries > 0) {
            console.warn(`Retrying request... (${retries} retries left)`);
            return new Promise<Response>((resolve) => setTimeout(resolve, retryDelay)).then(() =>
                fetchWithTimeout(url, options, timeout, retries - 1, retryDelay),
            );
        } else {
            throw error;
        }
    });
}

export function POST({
    url,
    data,
    timeout = 5000,
    retries = 0,
    retryDelay = 1000,
    options,
}: fetchProps): Promise<Response> {
    const mergedOptions = {
        ...postDefaultOptions,
        ...options,
        body: JSON.stringify(data),
    };
    return fetchWithTimeout(url, mergedOptions, timeout, retries, retryDelay);
}

export function GET({ url, timeout = 5000, retries = 0, retryDelay = 1000, options }: fetchProps): Promise<Response> {
    return fetchWithTimeout(url, options ? options : getDefaultOptions, timeout, retries, retryDelay);
}