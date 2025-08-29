<script>
    export let paths;

    function findMaxLength(paths) {
        let longestFound = 0;
        if (paths) {
            Object.keys(paths).forEach(path => {
                Object.keys(paths[path]).forEach(method => {
                    if (method.length > longestFound) {
                        longestFound = method.length;
                    }
                })
            })
        }
        return longestFound;
    }

    function padRight(value, length) {
        let padded = value;

        while (padded.length < length) {
            padded += ' ';
        }

        return padded;
    }

    const selectOperation = (event) => {
        let selected = jQuery(event.target).parents("[data-operation]:first").data('operation');

        selectOperationAndScrollTo(selected);
    }

    $: padTo = findMaxLength(paths);

    const initTooltip = el => {
        initTooltipBox(el);
    }

    function getAccessControlInfo(method) {
        let accessControl = {};

        if (method.security) {
            accessControl.active = true;

            let roles = [];
            Object.keys(method.security).forEach(accessOptionKey => {
                let accessOption = method.security[accessOptionKey];

                if (accessOption['Roles']) {
                    accessOption['Roles'].forEach(accessOptionValue => {
                        if (!roles.includes(accessOptionValue)) {
                            roles.push(accessOptionValue);
                        }
                    })
                }
            })

            accessControl.roles = roles.toSorted();
        }

        return accessControl;
    }

</script>

{#if paths}
    <div class="card card-primary">
        <div class="card-body path-toc">
            <ul class="path-toc-list">

                {#each Object.keys(paths).sort() as path}
                    {#each Object.keys(paths[path]) as method}
                        <li class="{paths[path][method].deprecated?'path-toc-deprecated':''} path-toc-item"
                            data-operation="{paths[path][method].operationId}"
                            on:click={selectOperation}>
                            <div>
                                <span location="" href="">
                                    <span class="path-toc-method">{padRight(method.toUpperCase(), padTo)} -</span>
                                    <span data-bs-container="body" data-bs-toggle="tooltip" use:initTooltip
                                          data-bs-placement="left" title=""
                                          data-bs-original-title="{paths[path][method].summary}"
                                          class="path-toc-path">{@html path.replaceAll("/", "/<span class='word-wrap'></span>")}</span>
                                </span>

                                {#each [getAccessControlInfo(paths[path][method])] as accessControl}
                                    {#if accessControl.active}
                                        <div class="path-toc-roles">
                                    <span class="badge" data-bs-container="body" data-bs-toggle="tooltip"
                                          use:initTooltip
                                          data-bs-placement="left" title="" data-bs-html="true"
                                          data-bs-original-title="Access Control"
                                    ><i class="fa-solid fa-lock"></i></span>
                                        </div>
                                    {/if}

                                    {#if accessControl.roles}
                                        <div class="path-toc-roles">
                                            {#each accessControl.roles as role}
                                             <span class="badge" data-parameter-highlight-name="role__{role}"
                                                   onmouseover="highlightParameter(this, event, true)"
                                                   onmouseout="deHighlightParameter(this, event, true)"
                                             >{role}</span>
                                            {/each}
                                        </div>
                                    {/if}
                                {/each}



                            </div>
                        </li>
                    {/each}
                {/each}

            </ul>
        </div>
    </div>
{/if}


<style>
    .path-toc-item {
        cursor: pointer;
    }

    .path-toc {
        font-family: monospace;
        font-size: 0.9rem;
        padding-bottom: 5px;
    }

    .path-toc li:hover .path-toc-path {
        text-decoration: underline;
    }

    .path-toc-deprecated {
        text-decoration: line-through;
    }

    .path-toc-list {
        list-style: none;
        margin-left: -29px;
        margin-bottom: 0;
    }

    .path-toc-method {
        float: left;
        white-space: pre;
        padding-right: 8px;
    }

    .path-toc-path {
        word-break: keep-all;
    }

    /* FIXME does not wrap in chrome */
    .word-wrap:after {
        content: "\200b";
    }

    .path-toc-roles {
        float: right;
    }

    .path-toc-roles span {
        margin-left: 5px;
    }

    .path-toc-roles .badge {
        background-color: #b46a02;
        color: black;
    }

</style>
