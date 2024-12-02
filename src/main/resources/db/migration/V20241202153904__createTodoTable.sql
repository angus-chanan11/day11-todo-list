create table if not exists todo_item (
    id int auto_increment primary key,
    text varchar(255),
    done boolean
)