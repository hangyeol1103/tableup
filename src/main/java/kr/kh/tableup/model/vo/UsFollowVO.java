package kr.kh.tableup.model.vo;

import java.util.List;

import lombok.Data;

@Data
public class UsFollowVO {
  private int uf_num;           // UF_NUM int AI PK
  private int uf_us_num;         // uf_us_num int
  private String uf_type;       // uf_TYPE enum('REVIEW','RESTAURANT')
  private int uf_foreign;       // uf_FOREIGN int

  private List<Integer> uf_foreign_list;
}
