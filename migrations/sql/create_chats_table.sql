create table if not exists chats(
	id bigint not null,
	created_at timestamp with time zone not null,
	primary key(id)
);
