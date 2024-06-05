"""Allow null for finished_time

Revision ID: f0d6bedef3f7
Revises: 
Create Date: 2024-05-26 14:31:09.793823

"""
from alembic import op
import sqlalchemy as sa
from sqlalchemy.dialects import mysql

# revision identifiers, used by Alembic.
revision = 'f0d6bedef3f7'
down_revision = None
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('tasks', schema=None) as batch_op:
        batch_op.alter_column('created_time',
               existing_type=mysql.VARCHAR(length=255),
               type_=sa.DateTime(timezone=255),
               existing_nullable=False)
        batch_op.alter_column('finished_time',
               existing_type=mysql.VARCHAR(length=255),
               type_=sa.DateTime(timezone=255),
               nullable=True)
        batch_op.alter_column('duration',
               existing_type=mysql.VARCHAR(length=255),
               nullable=True)

    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('tasks', schema=None) as batch_op:
        batch_op.alter_column('duration',
               existing_type=mysql.VARCHAR(length=255),
               nullable=False)
        batch_op.alter_column('finished_time',
               existing_type=sa.DateTime(timezone=255),
               type_=mysql.VARCHAR(length=255),
               nullable=False)
        batch_op.alter_column('created_time',
               existing_type=sa.DateTime(timezone=255),
               type_=mysql.VARCHAR(length=255),
               existing_nullable=False)

    # ### end Alembic commands ###
