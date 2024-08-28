{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "eric-odp-ssh-proxy.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create image registry url
*/}}
{{- define "eric-odp-ssh-proxy.registryUrl" -}}
{{- if .Values.global.registry.url -}}
{{- print .Values.global.registry.url -}}
{{- else -}}
{{- print .Values.imageCredentials.registry.url -}}
{{- end -}}
{{- end -}}

{{/*
Create image pull secrets
*/}}
{{- define "eric-odp-ssh-proxy.pullSecrets" -}}
{{- if .Values.global.pullSecret -}}
{{- print .Values.global.pullSecret -}}
{{- else if .Values.imageCredentials.pullSecret -}}
{{- print .Values.imageCredentials.pullSecret -}}
{{- end -}}
{{- end -}}

{{/*
Generate labels
*/}}
{{- define "eric-odp-ssh-proxy.metadata_app_labels" }}
app: {{ .Values.service.name | quote }}
app.kubernetes.io/name: {{ .Values.service.name | quote }}
app.kubernetes.io/instance: {{ .Release.Name | quote }}
app.kubernetes.io/version: {{ template "eric-odp-ssh-proxy.chart" . }}
{{- if .Values.labels }}
{{ toYaml .Values.labels }}
{{- end }}
{{- end }}

{{/*
Create replicas
*/}}
{{- define "eric-odp-ssh-proxy.replicas" -}}
{{- $replica_SG_name := printf "%s-%s" "replicas" .Values.ssh_broker.name -}}
{{- if index .Values "ssh_broker" $replica_SG_name -}}
{{- print (index .Values "ssh_broker" $replica_SG_name) -}}
{{- end -}}
{{- end -}}

{{/*
Generate product name
*/}}
{{- define "eric-odp-ssh-proxy.productName" -}}
{{- $product_name := printf "%s-%s" "helm" .Chart.Name -}}
{{- print $product_name -}}
{{- end -}}

Expand the name of the chart.
*/}}
{{- define "eric-odp-ssh-proxy.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Generate product number
*/}}
{{- define "eric-odp-ssh-proxy.productNumber" -}}
{{- if .Values.productNumber -}}
{{- print .Values.productNumber -}}
{{- else if .Values.productInfo -}}
{{- print .Values.productInfo.number -}}
{{- end -}}
{{- end -}}

{{/*
Generate product revision
*/}}
{{- define "eric-odp-ssh-proxy.productRevision" -}}
{{- if .Values.productRevision -}}
{{- print .Values.productRevision -}}
{{- else if .Values.productInfo -}}
{{- print .Values.productInfo.rstate -}}
{{- end -}}
{{- end -}}

{{/*
Generate Product info
*/}}
#product-info for configmap is resides inside _configmap.yaml
#If any change in the below product-info. Its Mandatory to change in the _configmap.yaml
{{- define "eric-odp-ssh-proxy.product-info" }}
ericsson.com/product-name: {{ default (include "eric-odp-ssh-proxy.productName" .) }}
ericsson.com/product-number: {{ default (include "eric-odp-ssh-proxy.productNumber" .) .Values.productNumber }}
ericsson.com/product-revision: {{ default (include "eric-odp-ssh-proxy.productRevision" .) .Values.productRevision }}
{{- end}}

{{- /*
eric-odp-ssh-proxy.util.merge will merge two YAML templates and output the result.

This takes an array of three values:
- the top context
- the template name of the overrides (destination)
- the template name of the base (source)
*/ -}}
{{- define "eric-odp-ssh-proxy.util.merge" -}}
{{- $top := first . -}}
{{- $overrides := fromYaml (include (index . 1) $top) | default (dict ) -}}
{{- $tpl := fromYaml (include (index . 2) $top) | default (dict ) -}}
{{- toYaml (merge $overrides $tpl) -}}
{{- end -}}
