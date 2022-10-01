package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id,money) values(?,?)";

        Connection con = null; //db연결
        PreparedStatement pstmt = null; //쿼리문삽입

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error",e);
            throw e;
        }
        // 외부 ip를 사용하것이기때문에 닫아줘야한다.
        finally {
        close(con,pstmt,null);
        }

    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }

        } catch (SQLException e) {
            log.error("error",e);
            throw e;
        }
        finally {
            close(con,pstmt,rs);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {

        if (rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("error",e);
            }
        }

        if (stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("error",e);
            }
        }

        if (con!=null){
            try {
                con.close();
            } catch (SQLException e) {
                log.error("error",e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }


}

