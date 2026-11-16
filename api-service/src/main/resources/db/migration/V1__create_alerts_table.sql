CREATE TABLE alerts (
    id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    type VARCHAR(60) NOT NULL,

    CONSTRAINT alerts_pkey PRIMARY KEY (id),

    CONSTRAINT alerts_status_check
        CHECK (status IN (
            'CREATED',
            'PROCESSING',
            'PROCESSED',
            'FAILED'
        ))
);
