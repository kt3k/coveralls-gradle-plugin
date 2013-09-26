#! /usr/bin/env groovy
// coveralls.groovy /

import groovy.json.*

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0-RC2')
import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.Method.POST

@Grab(group='org.apache.httpcomponents', module='httpmime', version='4.3') 
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.ContentType

API_HOST = 'https://coveralls.io'
API_PATH = '/api/v1/jobs'

COBERTURA_REPORT_PATH = 'build/reports/cobertura/coverage.xml'


// model for coveralls io report's source file report
class SourceReport {
    String name;
    String source;
    List<Integer> coverage;

    public SourceReport(String name, String source, List<Integer> coverage) {
        this.name = name;
        this.source = source;
        this.coverage = coverage;
    }

}

// model for coveralls io report
class Report {
    String service_job_id;
    String service_name;
    List<SourceReport> source_files;

    public Report() {
    }

    public Report(String serviceName, String serviceJobId, List<SourceReport> sourceFiles) {
        this.service_name = serviceName;
        this.service_job_id = serviceJobId;
        this.source_files = sourceFiles;
    }

    public String toJson() {
        JsonBuilder json = new JsonBuilder(this)
        return json.toString()
    }
}

class SourceReportFactory {

    public static List<SourceReport> createFromCoberturaXML(File file) {
        Node coverage = new XmlParser().parse(file)
        String sourceDir = coverage.sources.source.text() + '/'

        Map a = [:]

        coverage.packages.package.classes.class.each() {
            Map cov = a.get(it.'@filename', [:])

            it.lines.line.each() {
                cov[it.'@number'.toInteger() - 1] = it.'@hits'.toInteger()
            }
        }

        List<SourceReport> reports = new ArrayList<SourceReport>()

        a.each { String filename, Map cov ->
            String source = new File(sourceDir + filename).text

            List r = [null] * source.readLines().size()
            cov.each { Integer line, Integer hits ->
                r[line] = hits
            }

            reports.add new SourceReport(filename, source, r)
        }

        return reports

    }
}

// model for ci service info
class ServiceInfo {
    String serviceName;
    String serviceJobId;

    public ServiceInfo(String serviceName, String serviceJobId) {
        this.serviceName = serviceName;
        this.serviceJobId = serviceJobId;
    }
}

class ServiceInfoFactory {

    public static createFromEnvVar() {

        if (System.getenv('TRAVIS') == 'true') {
            return new ServiceInfo('travis-ci', System.getenv('TRAVIS_JOB_ID'))
        }

        // cannot create service info from env var
        return null

    }

}

void postToCoveralls(String json) {

    HTTPBuilder http = new HTTPBuilder(API_HOST + API_PATH)

    http.request(POST) { req ->

        req.entity = MultipartEntityBuilder.create().addBinaryBody('json_file', json.getBytes('UTF-8'), ContentType.APPLICATION_JSON, 'json_file').build()

        response.success = { resp, reader ->
            println resp.statusLine
            println resp.getAllHeaders()
            println resp.getData()
            System.out << reader
        }

        response.failure = { resp, reader ->
            println resp.statusLine
            println resp.getAllHeaders()
            println resp.getData()
            System.out << reader
        }
    }
}

void main() {
    ServiceInfo serviceInfo = ServiceInfoFactory.createFromEnvVar()

    if (serviceInfo == null) {
        println 'no available service'

        return
    }

    println 'service name: ' + serviceInfo.serviceName
    println 'service job id: ' + serviceInfo.serviceJobId

    File file = new File(COBERTURA_REPORT_PATH)

    if (!file.exists()) {
        println 'covertura report not available: ' + file.absolutePath

        return
    }

    println 'cobertura report file: ' + file.absolutePath

    List<SourceReport> sourceReports = SourceReportFactory.createFromCoberturaXML file

    Report rep = new Report(serviceInfo.serviceName, serviceInfo.serviceJobId, sourceReports)

    println rep.toJson()

    postToCoveralls rep.toJson()
}

main()
