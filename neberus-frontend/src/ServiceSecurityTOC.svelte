<script>

    export let openApi;

    const selectScheme = (event) => {
        let selected = event.target.dataset.scheme;

        selectSchemeAndScrollTo(selected);

        jQuery('.scheme > .highlight').removeClass('highlight');
        jQuery('#scheme-' + selected + ' > .card-header').addClass('highlight');
    }

</script>


{#if openApi && openApi.paths && openApi.components.securitySchemes}


    <div id="nav-access-control" class="list-group">
        <span class="list-group-item list-group-item-action}"
           data-scheme=""
           on:click={selectScheme}>
            Access Control
        </span>
        {#each Object.keys(openApi.components.securitySchemes).sort() as schemeKey}
            <span class="list-group-item list-group-item-action list-group-item-indent html-content"
                  data-scheme={schemeKey} on:click={selectScheme}>
                {#if openApi.components.securitySchemes[schemeKey].extensions && openApi.components.securitySchemes[schemeKey].extensions['x-title']}
                    {@html openApi.components.securitySchemes[schemeKey].extensions['x-title']}
                {:else}
                   {schemeKey}
                {/if}
            </span>
        {/each}

    </div>
{/if}


<style>

    .list-group-item {
        font-size: 0.9em;
        padding: 0.5rem 0.75rem;
        border: 1px solid rgba(0,0,0,0.25);
    }

    .list-group-item-indent {
        padding-left: 25px;
    }

</style>
