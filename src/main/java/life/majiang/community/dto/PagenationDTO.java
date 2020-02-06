package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PagenationDTO {
    private boolean showfirst;
    private boolean shownext;
    private boolean showprevious;
    private boolean showend;
    private List<Integer> pages = new ArrayList<>();
    private List<QuestionDTO> questionDTO;
    private Integer page;
    private Integer totalpage;

    public void setPagenation(Integer totalpage,Integer page) {
        //page当前第几页，totalpage总共多少页

        this.totalpage=totalpage;
        this.page = page;//当前类的page等于传过来的page
        pages.add(page);
        for (int i = 1; i <= 3; i++) {  //前端只显示7个分页，前面三个后面三个
            if (page - i > 0) {       //符合几次if前面就有几个分页，最多三个
                pages.add(0, page - i);
            }
            if (page + i <= totalpage) {//符合几次if后面就有几个分页，最多三个
                pages.add(page + i);
            }
        }
        //是否显示上一页
        if (page == 1) {
            showprevious = false;
        } else {
            showprevious = true;
        }
        //是否显示下一页
        if (page == totalpage) {
            shownext = false;
        } else {
            shownext = true;
        }
        //是否显示第一页按钮
        if (pages.contains(1)) {
            showfirst = false;
        } else {
            showfirst = true;
        }
        //是否显示最后一页按钮
        if (pages.contains(totalpage)) {
            showend = false;
        } else {
            showend = true;
        }
    }

}
