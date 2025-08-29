<script>
    import {afterUpdate} from "svelte";
    import queryString from "query-string";

    export let openApi;

    const predefinedKeys = ["type", "in", "name", "scheme"];
    const nonCustomKeys = ["type", "in", "name", "scheme", "description", "extensions"];

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

    function parseRoles(openApi) {

        let paths = Object.keys(openApi.paths);
        let operationsByRole = {};

        paths.forEach(path => {

            let methods = Object.keys(openApi.paths[path]);


            methods.forEach(method => {

                let accessControlInfo = getAccessControlInfo(openApi.paths[path][method]);


                if (!accessControlInfo.roles) {
                    accessControlInfo.roles = ["__undefined"];
                }
                    accessControlInfo.roles.forEach(role => {
                        if (!Object.keys(operationsByRole).includes(role)) {
                            operationsByRole[role] = [];
                        }

                        operationsByRole[role].push({
                            method: method,
                            path: path,
                            operationId: openApi.paths[path][method].operationId,
                            resource: openApi.paths[path][method].tags.filter(tag => tag.startsWith("resource:")).map(tag => tag.split(':')[1])[0],
                            summary: openApi.paths[path][method].summary
                        });
                    });



            });
        });

        return operationsByRole;
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

    function findMaxLength(operations) {
        let longestFound = 0;

        if (operations) {
            operations.forEach(operation => {
                if (operation.method.length > longestFound) {
                    longestFound = operation.method.length;
                }
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

    const initTooltip = el => {
        initTooltipBox(el);
    }

    afterUpdate(async() => {
        if (window) {
            let queryParams = queryString.parse(location.search);
            if (queryParams['access-control']) {
                scrollToHeading('access-control-container');
            } else if (!queryParams) {
                scrollToTop();
            }
        }
    })

</script>

{#if openApi && openApi.paths && openApi.components.securitySchemes}
    <div class="card card-primary access-control-container" id="access-control-container">
        <h3>Access Control</h3>

        {#if openApi.components.extensions && openApi.components.extensions['x-security-schemes-description']}
            <div class="card card-primary description-container">
                <div class="card-body html-content">{@html openApi.components.extensions['x-security-schemes-description']}</div>
            </div>
        {/if}

        {#each Object.keys(openApi.components.securitySchemes).sort() as schemeKey}
            <div class="card card-primary scheme-container">
                <div id="access-control-{schemeKey}">
                    {#if openApi.components.securitySchemes[schemeKey].extensions && openApi.components.securitySchemes[schemeKey].extensions['x-title']}
                        <span><strong class="d-inline-block html-content">{@html openApi.components.securitySchemes[schemeKey].extensions['x-title']}</strong><span class="badge security-item-title">{schemeKey}</span></span>
                    {:else}
                       <span><strong>{schemeKey}</strong></span>
                    {/if}
                    {#each predefinedKeys as key}
                        {#if openApi.components.securitySchemes[schemeKey][key]}
                            <span class="badge security-item-attribute">{key}: </span><span
                                class="badge security-item-attribute-value">{openApi.components.securitySchemes[schemeKey][key]}</span>
                        {/if}
                    {/each}
                </div>

                {#if openApi.components.securitySchemes[schemeKey]['description']}
                    <div class="scheme-description html-content">{@html openApi.components.securitySchemes[schemeKey]['description']}</div>
                {/if}

                {#if Object.keys(openApi.components.securitySchemes[schemeKey]).filter(k => !nonCustomKeys.includes(k)).length > 0}
                    <div class="card card-primary scheme-attributes-container">
                        <div class="card-body">
                            {#each Object.keys(openApi.components.securitySchemes[schemeKey]).filter(k => !nonCustomKeys.includes(k)) as entryKey}
                                <div class="row w-100">
                                    <div class="col-4"><span class="badge bg-secondary">{entryKey}</span></div>
                                    <div class="col-8 html-content">{@html openApi.components.securitySchemes[schemeKey][entryKey]}</div>
                                </div>
                            {/each}
                        </div>
                    </div>
                {/if}

                {#if schemeKey === 'Roles'}
                    {#each [parseRoles(openApi)] as operationsByRole}
                        {#if operationsByRole && Object.keys(operationsByRole).length > 0}

                            <div class="roles-container">

                                {#each Object.keys(operationsByRole).sort() as role}
                                    <div class="card card-primary">
                                        <div class="card-body">

                                            <div class="row w-100">
                                                {#if role === "__undefined"}
                                                    <div class="role col-4 noselect collapsed" data-bs-toggle="collapse"
                                                         data-bs-target=".roles-overview-{role}"
                                                         aria-expanded="false">
                                                        <span>Resources without Role-based access</span>
                                                        <span>
                                                            <i class="icon-toggle fas fa-angle-right"></i>
                                                        </span>
                                                    </div>
                                                {:else}
                                                    <div class="role col-4">
                                        <span class="badge" data-bs-container="body"
                                              data-bs-toggle="tooltip" data-bs-custom-class="html-content"
                                              use:initTooltip
                                              data-bs-placement="top" title="" data-bs-html="true"
                                              data-bs-original-title="{getRoleDescription(role)}">{role}</span>
                                                    </div>
                                                {/if}
                                                <div class="role-resources col-8 roles-overview-{role} {role === '__undefined' ? 'collapse' : ''} ">

                                                    {#each operationsByRole[role] as operation}
                                                        <div>
                                                            <a href="?resource={operation.resource}&operation={operation.operationId}"
                                                               data-bs-container="body" data-bs-toggle="tooltip" use:initTooltip
                                                               data-bs-placement="top" title={operation.summary}>
                                                                {padRight(operation.method.toUpperCase(), findMaxLength(operationsByRole[role]))} - {@html operation.path.replaceAll("/", "/<span class='word-wrap'></span>")}
                                                            </a>
                                                        </div>
                                                    {/each}

                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                {/each}

                            </div>

                        {/if}
                    {/each}
                {/if}
            </div>
        {/each}

    </div>

{/if}

<style>

    .role .badge {
        background-color: #b46a02;
        color: black;
        margin-right: 5px;
    }

    .role-resources div {
        font-family: monospace;
        font-size: 0.9rem;
        white-space: preserve;
    }

    .role-resources a:hover {
        text-decoration: underline;
    }

    .access-control-container, .scheme-container {
        min-height: 20px;
        padding: 19px;
        margin-bottom: 10px;
    }

    .roles-container {
        margin-top: 10px;
    }

    .roles-container .card, .description-container.card, .scheme-attributes-container.card {
        border: 0px;
        box-shadow: 4px 4px 10px 0px #0000007a;
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

    .security-item-title.badge {
        background-color: #515860;
        margin-left: 5px;
    }

    .word-wrap:after {
        content: "\200b";
    }

    .scheme-description {
        margin-top: 10px;
    }

</style>
