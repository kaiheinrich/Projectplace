import React from "react";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import CardActions from "@material-ui/core/CardActions";
import Button from "@material-ui/core/Button";
import makeStyles from "@material-ui/core/styles/makeStyles";

export default function ProjectOverviewItem({project}) {

    const classes = useStyles();
    //const history = useHistory();

    return (
        <Card className={classes.root}>
            <CardContent>
                <Typography variant="h5" component="h2">
                    {project.title}
                </Typography>
                <Typography className={classes.pos} color="textSecondary">
                    by {project.projectOwner}
                </Typography>
                <Typography variant="body2" component="p">
                    {project.description}
                </Typography>
            </CardContent>
            <CardActions>
                <Button size="small" >Learn More</Button>
            </CardActions>
        </Card>
    );
}

const useStyles = makeStyles({
    root: {
        minWidth: 275,
    },
    name: {
        fontSize: 20
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
});