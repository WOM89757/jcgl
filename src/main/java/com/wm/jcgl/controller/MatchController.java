package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.*;
import com.wm.jcgl.service.*;
import com.wm.jcgl.vo.MatchVo;
import com.wm.sys.common.*;
import com.wm.sys.entity.Dept;
import com.wm.sys.entity.User;
import com.wm.sys.service.DeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 教材匹配信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
@RestController
@RequestMapping("match")
public class MatchController {

    @Resource
    private MatchService matchService;
    @Resource
    private OrderService orderService;
    @Resource
    private BookService bookService;
    @Resource
    private DeptService deptService;
    @Resource
    private ResultService resultService;
    @Resource
    private LackService lackService;
    @Resource
    private BackService backService;



    /**
     * 根据期号id加载学校json数据
     */
    @RequestMapping("loadDeptTreeJsonByOrderId")
    public DataGridView loadDeptTreeJsonByOrderId(MatchVo matchVo) {
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();
//        添加部门条件
        if(null!=matchVo.getOrderId()){
//            this.matchFunction(matchVo.getOrderId());
            queryWrapper.groupBy("dept_id");
            queryWrapper.eq("order_id",matchVo.getOrderId());
            queryWrapper.select("dept_id");
            List<Match> list = this.matchService.list(queryWrapper);
            if(list.size()>0){
                List<TreeNode> treeNodes=new ArrayList<>();
                Dept deptManger  = this.deptService.getById(1);
                treeNodes.add(new TreeNode(deptManger.getId(), deptManger.getPid(), deptManger.getTitle(), deptManger.getOpen()==1?true:false));
                for (Match match : list) {
                    //根据id查询学校名
                    if(null!=match.getDeptId()){
                        Dept dept  = this.deptService.getById(match.getDeptId());
                        if(null!=dept) {
                            match.setSchoolname(dept.getTitle().replace(" ", ""));
                            Boolean spread=dept.getOpen()==1?true:false;
                            treeNodes.add(new TreeNode(dept.getId(), dept.getPid(), dept.getTitle(), spread));
                        }
                    }
                }
                return new DataGridView(treeNodes);
            }
            else{
                Integer code = Constast.ERROR;
                String msg = "无数据";

                return  new DataGridView(code,msg);
            }
        }

        return new DataGridView(null);
    }

    /**
     * 教材匹配算法，初次调用用于生成数据，之后用于更新数据
     * @param orderId
     */
    public void matchFunction(Integer orderId){
        //查询书目缺少详细信息
        QueryWrapper<Match> lqueryWrapper = new QueryWrapper<>();
        lqueryWrapper.select("order_id","dept_id", "book_id", "lNum AS number");
        lqueryWrapper.eq("order_id", orderId);
        lqueryWrapper.eq("type", 1);   //类型为缺少
        List<Match> lMatchList = this.matchService.list(lqueryWrapper);
        Iterator<Match> lIterator = lMatchList.iterator();

        // 查询书目剩余详细信息
        QueryWrapper<Match> bqueryWrapper = new QueryWrapper<>();
        bqueryWrapper.select("order_id","dept_id", "book_id", "bNum AS number");
        bqueryWrapper.eq("order_id", 5);
        bqueryWrapper.eq("type", 0);   //类型为剩余
        List<Match> bMatchList = this.matchService.list(bqueryWrapper);
        Iterator<Match> bIterator = bMatchList.iterator();

        //获取操作用户
        User user = (User) WebUtils.getSession().getAttribute("user");
        List<Lack> lList = new ArrayList<Lack>();
        List<Back> bList = new ArrayList<Back>();
        List<Result> resultsList = new ArrayList<Result>();
        while(lIterator.hasNext()){
            Match l_info = lIterator.next();
            // 没有退货信息 缺货信息加入缺货单
            if(bMatchList.size()==0){
                Lack lack = new Lack();
                lack.setBookId(l_info.getBookId());
                lack.setOrderId(l_info.getOrderId());
                lack.setDeptId(l_info.getDeptId());
                lack.setNumber(l_info.getNumber());
                lack.setOperName(user.getName());
                lack.setCreateTime(new Date());
                lList.add(lack);
                lIterator.remove();
            }
            //有退货信息，根据退货信息匹配缺货信息
            while(bIterator.hasNext()){
                Match b_info = bIterator.next();
                if(b_info.getBookId() == l_info.getBookId()){
                    Integer remainNum = b_info.getNumber() - l_info.getNumber();
                    if(remainNum > 0 || remainNum == 0){
                        Result result = new Result();
                        result.setOrderId(l_info.getOrderId());
                        result.setBdeptId(b_info.getDeptId());
                        result.setLdeptId(l_info.getDeptId());
                        result.setBookId(l_info.getBookId());
                        result.setNumber(b_info.getNumber()-remainNum);
                        resultsList.add(result);
                        lIterator.remove();
                        b_info.setNumber(remainNum);
                        if(remainNum==0){	bIterator.remove(); } //移除数量为零的退货信息
                        break;      //该条缺货记录已匹配完成，结束循环，跳出循环体，匹配下一条缺货数据
                    }
                    else{
                        if(b_info.getNumber()==0){ bIterator.remove(); break;}
                        Result result = new Result();
                        result.setOrderId(l_info.getOrderId());
                        result.setBdeptId(b_info.getDeptId());
                        result.setLdeptId(l_info.getDeptId());
                        result.setBookId(l_info.getBookId());
                        result.setNumber(b_info.getNumber());
                        resultsList.add(result);
                        l_info.setNumber(l_info.getNumber()-b_info.getNumber());
                        bIterator.remove();//移除数量为零的退货信息
                        continue;
                    }
                }
            }
            // 没有可以匹配的退货信息 缺货信息加入缺货单
            if(!bIterator.hasNext()){
                Lack lack = new Lack();
                lack.setBookId(l_info.getBookId());
                lack.setOrderId(l_info.getOrderId());
                lack.setDeptId(l_info.getDeptId());
                lack.setNumber(l_info.getNumber());
                lack.setOperName(user.getName());
                lack.setCreateTime(new Date());
                lList.add(lack);
                lIterator.remove();
            }
        }
        if(bMatchList.size()>0) {
            //缺数小于余数 资源剩余  退货信息加入退货单
            Iterator<Match> iterator = bMatchList.iterator();
            while(iterator.hasNext()) {
                Match b_info = iterator.next();
                Back back = new Back();
                back.setBookId(b_info.getBookId());
                back.setOrderId(b_info.getOrderId());
                back.setDeptId(b_info.getDeptId());
                back.setNumber(b_info.getNumber());
                back.setOperName(user.getName());
                back.setCreateTime(new Date());
                bList.add(back);
                iterator.remove();
            }
        }
//        System.out.println(resultsList);
//        System.out.println(lList);
//        System.out.println(bList);
        if(resultsList.size()>0){
            QueryWrapper<Result> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id",orderId);
            if(this.resultService.count()>0){
                this.resultService.remove(queryWrapper);
            }
            this.resultService.saveBatch(resultsList);
        }
        if(lList.size()>0){
            QueryWrapper<Lack> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id",orderId);
            this.lackService.remove(queryWrapper);
            this.lackService.saveBatch(lList);
        }
        if(bList.size()>0){
            QueryWrapper<Back> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id",orderId);
            this.backService.remove(queryWrapper);
            this.backService.saveBatch(bList);
        }
    }
    /**
     * 查询各学校余缺量
     */
    @RequestMapping("loadMatchResult")
    public List<Line> loadMatchResult(MatchVo matchVo) {

        QueryWrapper<Result> idsqueryWrapper = new QueryWrapper<>();
        idsqueryWrapper.select("bdept_id");
        idsqueryWrapper.eq("order_id",matchVo.getOrderId());
        idsqueryWrapper.groupBy("bdept_id");
        List<Result> idslist = this.resultService.list(idsqueryWrapper);
        List<Integer> ids = new ArrayList<Integer>();
        for (Result result:idslist) {
            ids.add(result.getBdeptId());
        }
        List allList = new ArrayList();
        for (Integer bId:ids) {

            QueryWrapper<Result> lidsqueryWrapper = new QueryWrapper<>();
            lidsqueryWrapper.select("ldept_id");
            lidsqueryWrapper.eq("order_id",matchVo.getOrderId());
            lidsqueryWrapper.eq("bdept_id",bId);
            lidsqueryWrapper.groupBy("ldept_id");
            List<Result> lidslist = this.resultService.list(lidsqueryWrapper);
            List<Integer> lids = new ArrayList<Integer>();
            for (Result result:lidslist) {
                lids.add(result.getLdeptId());
            }

            List<matchResult> list = new ArrayList<matchResult>();
            for (Integer lId:lids) {
                QueryWrapper<Result> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("bdept_id","ldept_id","book_id","number");
                queryWrapper.eq("order_id",matchVo.getOrderId());
                queryWrapper.eq("bdept_id",bId);
                queryWrapper.eq("ldept_id",lId);
                List<Result> resultList = this.resultService.list(queryWrapper);
                String fromName = null;
                String toName = null;
                List<matchbook> bookList = new ArrayList<>();
                for (Result result:resultList) {
                    //根据id查询学校名
                    if(null!=result.getBdeptId()){
                        Dept bdept  = this.deptService.getById(result.getBdeptId());
                        Dept ldept  = this.deptService.getById(result.getLdeptId());
                        if(null!=bdept && null!=ldept) {
                            result.setBDeptName(bdept.getTitle().replace(" ", ""));
                            result.setLDeptName(ldept.getTitle().replace(" ", ""));
                        }
                    }
                    if(null!=result.getBookId()){
                        Book book  = this.bookService.getById(result.getBookId());
                        if(null!=book) {
                            result.setBookName(book.getName());
                        }
                    }
                    fromName = result.getBDeptName();
                    toName = result.getLDeptName();
                    bookList.add(new matchbook(result.getBookName(),result.getNumber()));
                }
                list.add(new matchResult(fromName,toName,bookList));
            }
            allList.add(list);
        }
        System.out.println(allList);
        return allList;
    }
    /**
     * 查询各学校余缺量
     */
    @RequestMapping("loadSchoolMatch")
    public List<Line> loadSchoolMatch(MatchVo matchVo) {
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();
//        添加部门条件
        queryWrapper.select("dept_id","SUM(lNum)AS lNum","SUM(bNum)AS bNum");
        if(null!=matchVo.getDeptId()){
            queryWrapper.select("grade","dept_id","SUM(lNum)AS lNum","SUM(bNum)AS bNum");
            queryWrapper.eq("dept_id",matchVo.getDeptId());
            queryWrapper.groupBy("grade");
        }
        queryWrapper.eq("order_id",matchVo.getOrderId());
        queryWrapper.groupBy("dept_id");
        List<Match> matchList = this.matchService.list(queryWrapper);
        List<Line> list = new ArrayList<Line>();
        for (Match match:matchList) {
            if(null!=matchVo.getDeptId()){
                list.add(new Line(match.getGrade(),match.getLNum(),match.getBNum()));
            }else {
                //根据id查询学校名
                if(null!=match.getDeptId()){
                    Dept dept  = this.deptService.getById(match.getDeptId());
                    if(null!=dept) {
                        match.setSchoolname(dept.getTitle().replace(" ", ""));
                    }
                }
                list.add(new Line(match.getSchoolname(),match.getLNum(),match.getBNum()));
            }
        }
        return list;
    }
    /**
     * 查询各书目余缺量
     */
    @RequestMapping("loadBookMatch")
    public List<Bar> loadBookMatch(MatchVo matchVo) {
//        SELECT book_id,SUM(lNum)AS lNum,SUM(bNum)AS bNum
//        FROM b_match
//        WHERE order_id=5
//        GROUP BY book_id
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();

        if(null!=matchVo.getDeptId()){
            //添加部门条件
            queryWrapper.select("dept_id");
            queryWrapper.eq("dept_id",matchVo.getDeptId());
            //添加年级条件
            if(StringUtils.isNotBlank(matchVo.getGrade())){
                queryWrapper.select("grade");
                queryWrapper.eq("grade",matchVo.getGrade());
                queryWrapper.groupBy("grade");
            }
        }
        queryWrapper.select("book_id","SUM(lNum)AS lNum","SUM(bNum)AS bNum");

        queryWrapper.eq("order_id",matchVo.getOrderId());
        queryWrapper.groupBy("book_id");
        List<Match> matchList = this.matchService.list(queryWrapper);
        List<Bar> list = new ArrayList<Bar>();
        for (Match match:matchList) {
             //根据id查询书名
             if(null!=match.getBookId()){
                 Book book  = this.bookService.getById(match.getBookId());
                 if(null!=book) {
                     match.setBookname(book.getName());
                     match.setGrade(book.getGrade());
                 }
             }
             list.add(new Bar(match.getBookname(),match.getLNum(),match.getBNum()));

        }
        return list;
    }

    /**
     * 查询
     */
    @RequestMapping("loadAllMatch")
    public DataGridView loadAllMatch(MatchVo matchVo) {
        IPage<Match> page = new Page<>(matchVo.getPage(), matchVo.getLimit());
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(null!=matchVo.getOrderId(), "order_id",matchVo.getOrderId());
//        if(StringUtils.isNotBlank(matchVo.getSchoolname())){
//            queryWrapper.inSql("dept_id","select id from sys_dept where sys_dept.title like '%"+matchVo.getSchoolname()+"%'");
//        }
//        if(StringUtils.isNotBlank(matchVo.getOrdername())){
//            queryWrapper.inSql("order_id","select id from b_order where b_order.comment like '%"+matchVo.getOrdername()+"%'");
//        }
//        if(StringUtils.isNotBlank(matchVo.getBookname())){
//            queryWrapper.inSql("book_id","select id from b_book where b_book.name like '%"+matchVo.getBookname()+"%'");
//        }
        User user = (User) WebUtils.getSession().getAttribute("user");
        if(1!=user.getDeptid()){
            //非销售店查询
            queryWrapper.eq("dept_id",user.getDeptid());
        }
        this.matchService.page(page, queryWrapper);
        List<Match> records = page.getRecords();
        for (Match match : records) {

            match.setNumber(match.getLNum()==0?match.getBNum():match.getLNum());
            //根据id查询征订期号名称
            if(null!=match.getOrderId()){
                Order order  = this.orderService.getById(match.getOrderId());
                if(null!=order) {
                    match.setOrdername(order.getComment());
                }
            }
            //根据id查询书名
            if(null!=match.getBookId()){
                Book book  = this.bookService.getById(match.getBookId());
                if(null!=book) {
                    match.setBookname(book.getName());
                    match.setGrade(book.getGrade());
                }
            }
            //根据id查询学校名
            if(null!=match.getDeptId()){
                Dept dept  = this.deptService.getById(match.getDeptId());
                if(null!=dept) {
                    match.setSchoolname(dept.getTitle());
                }
            }
        }
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addMatch")
    public ResultObj addMatch(MatchVo matchVo) {
        try {
            matchVo.setCreateTime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            matchVo.setOperName(user.getName());
            matchVo.setStatus(Constast.STATUS_FALSE);

            //matchVo.setDeptId(user.getDeptid());
            if(matchVo.getType()==1){
                matchVo.setLNum(matchVo.getNumber());
            }else
            {
                matchVo.setBNum(matchVo.getNumber());
            }
            this.matchService.save(matchVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    /**
     * 修改
     */
    @RequestMapping("updateMatch")
    public ResultObj updateMatch(MatchVo matchVo) {
        try {
//            Match temp = this.matchService.getById(matchVo.getId());
//            if(temp.getType()==matchVo.getType()){
//                if(matchVo.getType()==1){
//                    matchVo.setLNum(matchVo.getNumber());
//                }else {
//                    matchVo.setBNum(matchVo.getNumber());
//                }
//            }else{
//                if(temp.getType()==1){
//                    matchVo.setLimit()
//                }
//            }

            if(matchVo.getType()==1){
                matchVo.setLNum(matchVo.getNumber());
                matchVo.setBNum(0);
            }else {
                matchVo.setBNum(matchVo.getNumber());
                matchVo.setLNum(0);
            }
                this.matchService.updateById(matchVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 提交确认
     */
    @RequestMapping("submitMatch")
    public ResultObj submitMatch(Integer id) {
        try {
            Match match = this.matchService.getById(id);
            match.setStatus(Constast.STATUS_TRUE);
            this.matchService.updateById(match);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }
    /**
     * 批量提交确认
     */
    @RequestMapping("batchSubmitMatch")
    public ResultObj batchSubmitMatch(MatchVo matchVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : matchVo.getIds()) {
                idList.add(id);
            }
            Collection<Match> matchList = this.matchService.listByIds(idList);
            for (Match match : matchList) {
                match.setStatus(Constast.STATUS_TRUE);
            }
            this.matchService.updateBatchById(matchList);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }

    /**
     * 撤回确认
     */
    @RequestMapping("withdrawMatch")
    public ResultObj  withdrawMatch(Integer id) {
        try {
            Match match = this.matchService.getById(id);
            match.setStatus(Constast.STATUS_FALSE);
            this.matchService.updateById(match);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteMatch")
    public ResultObj deleteMatch(Integer id) {
        try {
            this.matchService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteMatch")
    public ResultObj batchDeleteMatch(MatchVo matchVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : matchVo.getIds()) {
                idList.add(id);
            }
            this.matchService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }




}

