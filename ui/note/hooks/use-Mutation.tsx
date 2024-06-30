import { useCallback, useMemo, useContext } from "react";

// TODO: useContext to reload the DOM
function useMutation<T>(mutationFn: (...args: any[]) => Promise<T>) {
    let loading = false;
    let error: Error | null = null;
    let ok = null;

    //TODO: after need complete this function
    //NOTE: effect father components and children components reload DOM

    const execute = useCallback(
        async (...args: any[]) => {
            loading = true;
            error = null;
            try {
                const result = await mutationFn(...args);
                ok = result;
                return result;
            } catch (err) {
                error = err as Error;
            } finally {
                loading = false;
            }
        },
        [mutationFn],
    );

    return useMemo(
        () => ({
            execute,
        }),
        [execute],
    );
}