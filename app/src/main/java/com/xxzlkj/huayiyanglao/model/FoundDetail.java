package com.xxzlkj.huayiyanglao.model;

import com.xxzlkj.licallife.model.ProjectDetail;
import com.xxzlkj.zhaolinshare.base.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现详情（活动详情）
 * Created by Administrator on 2017/4/24.
 */

public class FoundDetail extends BaseBean {

    /**
     * data : {"id":"1","title":"中老年夕阳回忆，最美不过夕阳红","price":"0.00","starttime":"1493541961","longitude":"116.435727","latitude":"39.92657","explain":"中老年夕阳回忆，最美不过夕阳红，有人说，最美不过夕阳红。的确，早晨的太阳照耀大地，人们欣喜若狂；黄昏也有余辉映照，色彩斑斓，同样有绚丽多彩的感觉。往往人们重视朝阳，而忽略了夕阳。其实最美的色彩在夕阳西下。你看，黄昏时刻，人们拖着疲惫的身子站在高处瞭望远方，就会看到一束束余光照耀着大地或海面。地上被映出了红的光亮，海水也被折射得五颜六色，煞是好看！这时风景最美，应验了夕阳的光辉。此时，想起了一句话，夕阳无限好，只是近黄昏。大人欢欣，小孩高呼。","contact":"联系电话：18888888888\r\n联系邮箱：18888888888@163.com","desc":"注意身体健康！","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492769770544750","address":"北京市朝阳区人保寿险大楼","count":"5","logo":"http://zhaolin-10029121.image.myqcloud.com/sample1492848450154299","name":"兆邻","phone":"010-84786448","state":"1","is_sign":"0","is_cell":"0","user":[{"id":"111339","username":"哈哈哈哈哈","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492496922223405650"},{"id":"111380","username":"和留峰","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492998282740024067"},{"id":"111341","username":"张魁","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492048636387827368"},{"id":"111355","username":"黄岗","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491642786314"},{"id":"100000","username":"php","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492841280990225537"}],"img":[{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample149276685370117","w":"536","h":"300"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492766853225765","w":"750","h":"380"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492766854777641","w":"536","h":"300"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492769771533335","w":"750","h":"380"}]}
     */

    private DataBean data = new DataBean();

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 中老年夕阳回忆，最美不过夕阳红
         * price : 0.00
         * starttime : 1493541961
         * longitude : 116.435727
         * latitude : 39.92657
         * explain : 中老年夕阳回忆，最美不过夕阳红，有人说，最美不过夕阳红。的确，早晨的太阳照耀大地，人们欣喜若狂；黄昏也有余辉映照，色彩斑斓，同样有绚丽多彩的感觉。往往人们重视朝阳，而忽略了夕阳。其实最美的色彩在夕阳西下。你看，黄昏时刻，人们拖着疲惫的身子站在高处瞭望远方，就会看到一束束余光照耀着大地或海面。地上被映出了红的光亮，海水也被折射得五颜六色，煞是好看！这时风景最美，应验了夕阳的光辉。此时，想起了一句话，夕阳无限好，只是近黄昏。大人欢欣，小孩高呼。
         * contact : 联系电话：18888888888
         联系邮箱：18888888888@163.com
         * desc : 注意身体健康！
         * simg : http://zhaolin-10029121.image.myqcloud.com/sample1492769770544750
         * address : 北京市朝阳区人保寿险大楼
         * count : 5
         * logo : http://zhaolin-10029121.image.myqcloud.com/sample1492848450154299
         * name : 兆邻
         * phone : 010-84786448
         * state : 1
         * is_sign : 0
         * is_cell : 0
         * user : [{"id":"111339","username":"哈哈哈哈哈","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492496922223405650"},{"id":"111380","username":"和留峰","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492998282740024067"},{"id":"111341","username":"张魁","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492048636387827368"},{"id":"111355","username":"黄岗","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1491642786314"},{"id":"100000","username":"php","simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492841280990225537"}]
         * img : [{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample149276685370117","w":"536","h":"300"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492766853225765","w":"750","h":"380"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492766854777641","w":"536","h":"300"},{"simg":"http://zhaolin-10029121.image.myqcloud.com/sample1492769771533335","w":"750","h":"380"}]
         */

        private String id;
        private String title;
        private String price;
        private String starttime;
        private String longitude;
        private String latitude;
        private String explain;
        private String contact;
        private String desc;
        private String simg;
        private String address;
        private String count;
        private String logo;
        private String name;
        private String phone;
        private String state;
        private String is_sign;
        private String is_cell;
        private String stoptime;
        private String sign;
        private String type;
        private List<SignUpUser> user = new ArrayList<>();
        private List<ProjectDetail.DataBean.ImgBean> img = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getIs_sign() {
            return is_sign;
        }

        public void setIs_sign(String is_sign) {
            this.is_sign = is_sign;
        }

        public String getIs_cell() {
            return is_cell;
        }

        public void setIs_cell(String is_cell) {
            this.is_cell = is_cell;
        }

        public List<SignUpUser> getUser() {
            return user;
        }

        public void setUser(List<SignUpUser> user) {
            this.user = user;
        }

        public List<ProjectDetail.DataBean.ImgBean> getImg() {
            return img;
        }

        public void setImg(List<ProjectDetail.DataBean.ImgBean> img) {
            this.img = img;
        }

        public String getStoptime() {
            return stoptime;
        }

        public void setStoptime(String stoptime) {
            this.stoptime = stoptime;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
