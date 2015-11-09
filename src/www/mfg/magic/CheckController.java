package www.mfg.magic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Properties;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author hezis
 *
 */
@RestController
@RequestMapping("/check")
public class CheckController {

    private String controlledMachineIp;

    @Value("${listening.port}")
    private String listeningPort;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/find", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
    public void getDynamicProps(HttpServletRequest httpRequest, HttpServletResponse response) throws IOException, ClassNotFoundException,
            ParseException {
        String msg = "No one is controlling the computer, go ahead ";
        Process process = Runtime.getRuntime().exec("netstat -n");
        Scanner sc = new Scanner(process.getInputStream());
        controlledMachineIp = InetAddress.getLocalHost().getHostAddress();
        String pcName = null;
        while (sc.hasNext() && sc.hasNextLine()) {
            // process each line in some way
            String nextLine = sc.nextLine();
            if (nextLine.contains(controlledMachineIp + ":" + listeningPort)) {
                String[] split = nextLine.split(" ");
                for (int i = 0; i < split.length; i++) {
                    String string = split[i];
                    if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(string, "130.138") && !string.equals(controlledMachineIp)) {
                        String[] split2 = string.split(":");
                        pcName = split2[0];
                    }
                }
                if (pcName != null) {
                    File file = new File("userFinder.properties");
                    file.createNewFile();
                    FileInputStream fileInput = new FileInputStream(file);
                    Properties properties = new Properties();
                    properties.load(fileInput);
                    fileInput.close();
                    String pcHolder = (String) properties.get(pcName);

                    if (pcHolder != null && pcName != null) {
                        msg = "The computer is being controlled by this user - " + pcHolder + ", from this pc - " + pcName + ".";
                        System.out.println(msg);
                    }
                    else {
                        msg = "The computer is being controlled by this pcName- " + pcName
                                + ", please check why properties file is not updated with this computer.";
                        System.out.println(msg);
                    }
                }
            }
        }
        sc.close();
        objectMapper.writeValue(response.getOutputStream(), msg);
    }

}
