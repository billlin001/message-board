package tw.bill.java101.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tw.bill.java101.dto.PageDataDto;
import tw.bill.java101.dto.TeamForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bill33 on 2016/5/27.
 */
@RestController
@RequestMapping("/rest")
public class TeamRestController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/teams", method = RequestMethod.GET)
    @ResponseBody
    public PageDataDto getTeamList(Pageable pageRequest) {
        log.info(pageRequest.toString());
        PageDataDto result = new PageDataDto();
        result.addData(new TeamForm());
        return result;
    }
}

