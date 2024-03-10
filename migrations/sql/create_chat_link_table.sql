create table if not exists chat_links (
	id_chat bigint not null references chats (id) on delete cascade,
	id_link bigint not null references links (id) on delete cascade,
	primary key (id_chat, id_link)
);
