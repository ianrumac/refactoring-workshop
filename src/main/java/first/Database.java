package first;

import java.util.ArrayList;
import java.util.List;

public class Database {


        public static Database getInstance() {
                return new Database();
        }

        public Object getObject(String column, Long id){
            return new Object();
        }

        public void putObject(Object object, String column, DBCallback callback){
            callback.success();
        }

        public List<?> getColumn(String columnName){
            return new ArrayList<>();
        }

        interface DBCallback{

            void success();
            void failure(Throwable throwable);

        }
}
