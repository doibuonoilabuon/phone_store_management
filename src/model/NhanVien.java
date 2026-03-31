
package model;




public class NhanVien {

    private int manv;
    private String hoten;
    private int gioitinh;
    private String sdt;
    private String chucvu;
    private int trangthai;
    private String email;

    public NhanVien() {
    }

    public NhanVien(int manv, String hoten, int gioitinh, String chucvu, String sdt, int trangthai, String email) {
        this.manv = manv;
        this.hoten = hoten;
        this.gioitinh = gioitinh;
        this.chucvu = chucvu;
        this.sdt = sdt;
        this.trangthai = trangthai;
        this.email = email;
    }

    public NhanVien(String hoten, int gioitinh, String chucvu, String sdt, int trangthai) {
        this.hoten = hoten;
        this.gioitinh = gioitinh;
        this.chucvu = chucvu;
        this.sdt = sdt;
        this.trangthai = trangthai;
     
    }

    public int getManv() {
        return manv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public int getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(int gioitinh) {
        this.gioitinh = gioitinh ;
    }

    public String getChucvu() {
        return chucvu;
    }

    public void setNgaysinh(String chucvu) {
        this.chucvu = chucvu;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
       
        return true;
    }

    @Override
    public String toString() {
        return "NhanVien{" + "manv=" + manv + ", hoten=" + hoten + ", gioitinh=" + gioitinh + ", chucvu=" + chucvu + '}';
    }

    public int getColumnCount() {
        return getClass().getDeclaredFields().length;
    }

}
