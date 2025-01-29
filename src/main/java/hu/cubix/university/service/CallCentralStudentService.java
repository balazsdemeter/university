package hu.cubix.university.service;

import hu.cubix.university.wsclient.SemesterServiceXmlWs;
import hu.cubix.university.wsclient.SemesterServiceXmlWsImplService;
import org.springframework.stereotype.Service;

@Service
public class CallCentralStudentService {

    public Integer getNumberOfFreeSemestersByStudentId(Integer studentId) {
        SemesterServiceXmlWs port = new SemesterServiceXmlWsImplService().getSemesterServiceXmlWsImplPort();
        return port.getNumberOfFreeSemestersByStudentId(studentId);
    }
}
