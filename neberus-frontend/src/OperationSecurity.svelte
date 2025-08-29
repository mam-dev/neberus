<script>

    export let operation;
    export let openApi;

    const initTooltip = el => {
        initTooltipBox(el);
    }

    function getSecurityScheme(type) {
        if (!openApi.components.securitySchemes) return undefined;
        return openApi.components.securitySchemes[type];
    }

    function getSecurityTypeDescription(type) {
        let securityScheme = getSecurityScheme(type);
        if (!securityScheme) return undefined;
        return securityScheme.description ?? undefined;
    }

    function getRoleDescription(role) {

        if (!openApi.components.securitySchemes.Roles
            || !openApi.components.securitySchemes.Roles.extensions
            || !openApi.components.securitySchemes.Roles.extensions['x-defined-roles']) {
            return undefined;
        }

        return openApi.components.securitySchemes.Roles.extensions['x-defined-roles']
            .filter(r => {
                return r.name === role;
            })
            .map(r => {
                return r.description
            })
            [0] ?? undefined;
    }

    function subdivide(array, maxChunkSize) {

        let chunkSize = array.length;
        while (chunkSize > maxChunkSize) {
            chunkSize /= 2;
        }

        let chunks = [];
        for (let i = 0; i < array.length; i += chunkSize) {
            const chunk = array.slice(i, i + chunkSize);
            chunks.push(chunk);
        }
        return chunks;
    }

</script>

{#if operation.security}
    <div class="card card-primary card-nested security-overview">

        <div class="card-header collapsed" data-bs-toggle="collapse" data-bs-target=".security-overview-{operation.operationId}"
             aria-expanded="false">
            <h6 class="d-inline-block"><i class="fas fa-lock pe-1"></i> Access Options</h6>
            <span>
                <i class="icon-toggle fas fa-angle-right"></i>
            </span>
            <span class="float-end"
                  data-bs-toggle="tooltip" data-bs-html="true" data-bs-placement="left" use:initTooltip
                  title="Each row shows an option to gain access.<br>Within each row, <strong>all</strong> entries need to be provided.">
                <span><i class="fas fa-info-circle"></i></span>
            </span>
        </div>

        <div class="card-body security-overview-{operation.operationId} collapse">

            {#each operation.security as securitySet, index}
                {#each [crypto.randomUUID()] as itemId}
                    <div class="card card-primary security-set {securitySet['x-deprecated'] ? 'deprecated' : ''}"
                         id="security-set-{itemId}">
                        <div class="card-header bg-secondary {index === 0 ? '' : 'collapsed'}"
                             data-bs-toggle="collapse" data-bs-target=".security-set-body-{itemId}"
                             aria-expanded="{index === 0 ? 'true' : 'false'}">
                            <i class="fas fa-key pe-1"></i>
                            {#if securitySet['x-title']}
                                <span class="security-set-title html-content">{@html securitySet['x-title'][0]}</span>
                            {/if}
                            <span>
                                <i class="icon-toggle fas fa-angle-right"></i>
                            </span>
                            {#if securitySet['x-description']}
                                <div class="security-set-body-{itemId} security-set-description collapse {index === 0 ? 'show' : ''}">
                                    <div class="html-content">
                                        {@html securitySet['x-description'][0]}
                                    </div>
                                </div>
                            {/if}
                            {#if securitySet['x-deprecated']}
                                <span class="float-end noselect opacity-75">[Deprecated]</span>
                            {/if}
                        </div>

                        <div class="card-body security-set-body-{itemId} collapse {index === 0 ? 'show' : ''}">

                            {#each subdivide(Object.keys(securitySet).filter(e => !e.startsWith('x-')), 3) as securityKeyChunk}
                                <div class="row">
                                    {#each securityKeyChunk as securityKey}
                                        <div class="col">
                                            <div class="card security-item">

                                                <div class="card-header" data-bs-container="body"
                                                     data-bs-toggle="tooltip" data-bs-custom-class="html-content"
                                                     use:initTooltip
                                                     data-bs-placement="top" title="" data-bs-html="true"
                                                     data-bs-original-title="{getSecurityTypeDescription(securityKey)}">

                                                    {#if openApi.components.securitySchemes[securityKey]}
                                                        <a href="?access-control={securityKey}">
                                                            {#if openApi.components.securitySchemes[securityKey].extensions && openApi.components.securitySchemes[securityKey].extensions['x-title']}
                                                                <span class="security-item-key d-inline-block html-content">{@html openApi.components.securitySchemes[securityKey].extensions['x-title']}</span><span class="badge">{securityKey}</span>
                                                            {:else}
                                                                <span class="security-item-key">{securityKey}</span>
                                                            {/if}
                                                        </a>
                                                    {:else}
                                                        <span class="security-item-key">{securityKey}</span>
                                                    {/if}

                                                    <div class="security-item-scheme">
                                                        {#each [getSecurityScheme(securityKey)] as scheme}
                                                            {#if scheme}
                                                                {#if scheme.in === 'HEADER' && scheme.name}
                                                                    <span class="badge security-item-attribute">Header</span><span class="badge security-item-attribute-value">{scheme.name}{scheme.scheme ? ': ' + scheme.scheme : ''}</span>
                                                                {/if}
                                                                {#if scheme.extensions && scheme.extensions['x-custom-type-attributes']}
                                                                    {#each Object.keys(scheme.extensions['x-custom-type-attributes']) as attrName}
                                                                        <span class="badge security-item-attribute">{attrName}</span><span class="badge security-item-attribute-value">{scheme.extensions['x-custom-type-attributes'][attrName]}</span>
                                                                    {/each}
                                                                {/if}
                                                            {/if}
                                                        {/each}
                                                    </div>
                                                </div>

                                                {#if securitySet[securityKey] && securitySet[securityKey].length > 0}
                                                    <div class="card-body">
                                                        {#each securitySet[securityKey] as itemValue}
                                                            {#if securityKey === 'Roles'}
                                                                <span class="security-item-value badge" data-bs-container="body"
                                                                      data-bs-toggle="tooltip" data-bs-custom-class="html-content"
                                                                      use:initTooltip
                                                                      data-bs-placement="top" title="" data-bs-html="true"
                                                                      data-bs-original-title="{getRoleDescription(itemValue)}">{itemValue}</span>
                                                            {:else}
                                                                <div>
                                                                    <span class="security-item-value badge" >{itemValue}</span>
                                                                </div>
                                                            {/if}
                                                        {/each}
                                                    </div>
                                                {/if}

                                            </div>
                                        </div>
                                    {/each}
                                </div>
                            {/each}

                        </div>
                    </div>


                {/each}
            {/each}
        </div>
    </div>
{/if}

<style>

    .card {
        border: 0px;
        box-shadow: 4px 4px 10px 0px #0000007a;
    }

    .security-overview > .card-header {
        background-color: #b46a02 !important;
    }

    .card-header h6 {
        margin-bottom: 0;
    }

    .security-set > .card-header:hover {
        background-color: #5f666c !important;
    }

    .role {
        margin-left: 10px;
        display: inline-block;
    }

    .security-set-description {
        margin: 0.5rem -1rem -0.55rem;
        padding: 0.5rem 1rem;
        background-color: #292d32 !important;
    }

    .security-item .card-header {
        background-color: #515860 !important
    }

    .security-item > .card-header:hover {
        background-color: #3e444c !important;
    }

    .security-item-key {
        font-weight: bold;
    }

    .security-item-scheme {
        margin: 0.5rem -1rem -0.5rem;
        padding: 0.5rem 1rem;
        background-color: #3e444c;
    }

    .security-item-scheme:empty, .security-item-scheme:not(:has(*)) {
        display: none;
    }

    .security-item-scheme div:not(:last-of-type) {
        margin-bottom: 4px;
    }

    .security-item-value.badge {
        background-color: #b46a02;
        color: black;
        margin-right: 5px;
    }

    .security-item-attribute.badge {
        background-color: #515860;
        border-top-right-radius: 0px;
        border-bottom-right-radius: 0px;
    }

    .security-item-attribute-value.badge {
        background-color: #292d32;
        border-top-left-radius: 0px;
        border-bottom-left-radius: 0px;
        margin-right: 5px;
    }

    .security-item-key + .badge {
        background-color: #292d32;
        margin-left: 5px;
    }

    .security-set-title {
        display: inline-block;
    }

</style>
