<script>
    import PathOverview from "./PathOverview.svelte";
    import Operation from "./Operation.svelte";
    import queryString from "query-string";
    import {afterUpdate, beforeUpdate} from "svelte";

    export let paths;
    export let openApi;

    afterUpdate(async () => {
        if (window) {
            let queryParams = queryString.parse(location.search);
            if (queryParams.operation) {
                selectOperationAndScrollTo(queryParams.operation);
            } else {
                scrollToTop();
            }
        }

        jQuery('button.btn-popover').each((idx, elem) => initPopover(elem));
    })

    let popoverElems = [];

    const initPopover = el => {
        let popoverElem = initPopoverButton(el);
        popoverElems.push(popoverElem);
    }

    beforeUpdate(() => {
        popoverElems.forEach(elem => {
            elem.dispose();
        });
        popoverElems = [];
    })

    function getOrderedOperations(paths) {
        let operations = [];

        Object.keys(paths).forEach(path => {
            Object.keys(paths[path]).forEach(method => {
                operations.push({
                    path: path,
                    method: method,
                    operation: paths[path][method],
                })
            });
        });

        operations.sort((a, b) => a.operation.extensions['x-order'] - b.operation.extensions['x-order']);

        return operations;
    }

</script>

<PathOverview paths={paths}/>

<div data-spy="scroll" data-bs-target="#nav-operations" data-offset="0" class="scrollspy-example">
    {#if paths}
        {#each getOrderedOperations(paths) as operation}
            <Operation path={operation.path} method={operation.method} operation={operation.operation} openApi={openApi}/>
        {/each}
    {/if}
</div>
