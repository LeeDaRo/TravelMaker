package bit.travelmaker.back.controller;

import bit.travelmaker.back.dto.out.OutPackageCard;
import bit.travelmaker.back.service.PackageBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/packageboard")
public class PackageBoardController {

    @Autowired
    private PackageBoardService packageBoardService;

    @GetMapping(value = "")
    public ResponseEntity<?> getPackage() {
        HttpStatus status = HttpStatus.OK;

        return new ResponseEntity<>(status);
    }

    /**
     * 패키지 상세보기 요청시 데이터를 전송해주는 메소드
     */
    @GetMapping(value = "/detail/{boardId}")
    public ResponseEntity<?> getDetailPackage(@PathVariable int boardId) {
        HttpStatus status = HttpStatus.OK;

        HashMap<String, Object> res = packageBoardService.getDetailPackage(boardId);

        return new ResponseEntity<>(res, status);
    }


    /**
     * 패키지 게시판에서 패키지 카드를 구성하기 위한 데이터를 전송해주는 메소드
     */
    @GetMapping(value = "/{pageNum}")
    public ResponseEntity<?> getPackageCardList(@PathVariable int pageNum){
        HashMap<String, Object> response = new HashMap<>();

        List<OutPackageCard> packageData = packageBoardService.PKCardPush(pageNum);
        int count = packageBoardService.packageCount();

        List<HashMap<String, Object>> packages = new ArrayList<>();

        for(int i = 0 ; i < packageData.size() ; i++) {
            HashMap<String, Object> item = packageData.get(i).convertToHashmap();

            packages.add(item);
        }

        response.put("count", count);
        response.put("packageData", packages);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/write")
    public ResponseEntity<?> writePackage(@RequestBody HashMap<String, Object> req) {
        HttpStatus status = HttpStatus.OK;

        System.out.println(req);

        return new ResponseEntity<>(req, status);
    }

    @PostMapping(value = "/wish")
    public ResponseEntity<?> doWish(@AuthenticationPrincipal int userId,@RequestBody int boardId) {
        HttpStatus status = HttpStatus.OK;

        HashMap<String, Object> req = new HashMap<>();

        req.put("userId", userId);
        req.put("boardId", boardId);

        Boolean res = packageBoardService.doWish(req);
        System.out.println(res);

        return new ResponseEntity<>(res, status);
    }

    @PostMapping(value = "/iswish")
    public ResponseEntity<?> isWish(@AuthenticationPrincipal int userId, @RequestBody List<Integer> req) {
        HttpStatus status = HttpStatus.OK;

        System.out.println("req");
        System.out.println(req);

        List<Integer> reqForm = new ArrayList<>();

        for (Integer i : req) {
            reqForm.add(i);
        }

        HashMap<String, Object> reqData = new HashMap<>();

        reqData.put("userId", userId);
        reqData.put("boardList", reqForm);

        List<HashMap<String, Object>> resData = packageBoardService.isWish(reqData);

        List<Integer> res = new ArrayList<>();

        for (HashMap<String, Object> item : resData) {
            res.add((Integer) item.get("boardid"));
        }
        System.out.println("res");
        System.out.println(res);


//        for(Integer i : req) {
////            packageBoardService.
//        }

        return new ResponseEntity<>(res, status);
    }
}
